package ma.zeldaboy111.FactoryHandler;

import com.google.common.annotations.VisibleForTesting;
import ma.zeldaboy111.FactoryMaterials;
import ma.zeldaboy111.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import sun.security.x509.InvalidityDateExtension;

public class Movement {
    private final double[] xIncrement = new double[] {1.0, 0.0, -1.0, 0.0};
    private final double[] zIncrement = new double[] {0.0, 1.0, 0.0, -1.0};

    private Stand stand;
    private Boolean moving;
    private Boolean hasMovedBefore;
    private Boolean updated;

    private Location nextLocation;

    private DirectionData currentDirection;
    private DirectionData nextDirection;
    private DirectionData secondDirection; // direction after 'nextDirection'

    double speed; // 0.0
    double maxSpeed; // 0.25

    public Movement(Stand stand) {
        this.stand = stand;
        this.hasMovedBefore = false;

        this.nextLocation = stand.getStandBlockLocation();

        this.currentDirection = new DirectionData(-1, null);
        this.nextDirection = new DirectionData(-1, null);
        this.secondDirection = new DirectionData(-1, null);
    }
    private void checkMove() {
        if(!checkIfStandIsAlive()) return;
        if(checkifNewMoveIsNeeded()) move();
        else setMoving(false);
    }

    private void move() {
        if(!checkIfStandIsAtLocation()) return;
        updated = false;
        if(!hasMovedBefore) checkFirstMovement();
        else checkSecondMovement();

        if(updated) stand.moveStand(xIncrement[currentDirection.getDirection()], 0.0, zIncrement[currentDirection.getDirection()]);
        else moving = false;
    }

    private void checkFirstMovement() {
        for(int checkingDirection = 0; checkingDirection < 4; checkingDirection++) {
            Location location = getCheckingLocation(checkingDirection);
            FactoryMaterials factoryPart = getFactoryPartAtLocation(location);
            if(factoryPart != null && checkIfNextDirIsValid(currentDirection.getDirection(), checkingDirection)) {
                updateFirstDirection(checkingDirection, factoryPart);
                break;
            }
        }
    }
    private void checkSecondMovement() {
        if(hasMovedBefore) currentDirection.updateData(nextDirection.getDirection(), nextDirection.getFactoryType());
        else hasMovedBefore = true;

        nextDirection.updateData(secondDirection.getDirection(), secondDirection.getFactoryType());
        for(int checkingDirection = 0; checkingDirection < 4; checkingDirection++) {
            Location location = getCheckingLocationSecond(checkingDirection);
            FactoryMaterials factoryPart = getFactoryPartAtLocation(location);
            if(factoryPart != null && checkIfNextDirIsValid(nextDirection.getDirection(), checkingDirection)) {
                updateSecondDirection(checkingDirection, factoryPart);
                break;
            }
            else secondDirection.updateData(-1, null);
        }
    }

    private void updateFirstDirection(int direction, FactoryMaterials factoryPart) {
        updated = true;
        currentDirection.updateData(direction, factoryPart);
        updateNextDirection();
    }
    private void updateSecondDirection(int direction, FactoryMaterials factoryPart) {
        updated = true;
        secondDirection.updateData(direction, factoryPart);
        updateNextDirection();
    }
    private void updateNextDirection() {
        nextDirection.updateData(-1, null);
        nextLocation.add(xIncrement[currentDirection.getDirection()], 0.0, zIncrement[currentDirection.getDirection()]);
    }

    private Boolean checkIfStandIsAlive() {
        Boolean alive = stand != null && !stand.getStand().isDead();
        if (!alive) setMoving(false);
        return alive;
    }
    private Boolean checkIfNextDirIsValid(int oldDirection, int checkingDirection) {
        int oppositeDirection = checkingDirection > 1 ? checkingDirection - 2 : checkingDirection + 2;
        return oppositeDirection != oldDirection;
    }
    private Boolean checkifNewMoveIsNeeded() { return nextDirection == null || nextDirection.getDirection() == -1; }
    private Boolean checkIfStandIsAtLocation() { return nextLocation != null && nextLocation.equals(stand.getStandBlockLocation()); }
    private Location getCheckingLocation(int direction) { return stand.getStandBlockLocation().add(xIncrement[direction], 1.0, zIncrement[direction]); }
    private Location getCheckingLocationSecond(int direction) { return nextLocation == null ? null : nextLocation.clone().add(xIncrement[direction], 1.0, zIncrement[direction]); }
    private FactoryMaterials getFactoryPartAtLocation(Location location) {
        Material mat = location.getBlock().getType();
        if (mat == FactoryMaterials.BELT.getMaterial()) return FactoryMaterials.BELT;
        return null;
    }

    private void setMoving(Boolean state) { moving = state; }

    public void startMoving() {
        moving = true;
        new BukkitRunnable() {
            public void run() {
                if(moving) checkMove();
                else cancel();
            }
        }.runTaskTimer(Main.plugin, 2, 3);
    }

}
