package ma.zeldaboy111.FactoryHandler;

import com.google.common.annotations.VisibleForTesting;
import ma.zeldaboy111.FactoryMaterials;
import ma.zeldaboy111.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import sun.security.x509.InvalidityDateExtension;

public class Movement {
    private final double[] xIncrement = new double[] {1.0, 0.0, -1.0, 0.0};
    private final double[] zIncrement = new double[] {0.0, 1.0, 0.0, -1.0};
    private final double maxSpeed = 0.25;
    private final double speedChange = 0.05;

    private Stand stand;
    private Boolean moving;
    private Boolean updated;
    private Boolean hasMovedBefore;
    private Boolean nextBlockWaiting;

    private Location nextLocation;
    private Location locationAfterNext;

    private DirectionData currentDirection;
    private DirectionData nextDirection;
    private DirectionData directionAfterNext; // direction after 'nextDirection'

    double speed;

    public Movement(Stand stand) {
        this.speed = 0.0;
        this.stand = stand;
        this.hasMovedBefore = false;
        this.nextBlockWaiting = false;

        this.nextLocation = stand.getLocation();
        this.locationAfterNext = nextLocation.clone();
        nextLocation.getBlock().setType(Material.CYAN_WOOL);

        this.currentDirection = new DirectionData(-1, null);
        this.nextDirection = new DirectionData(-1, null);
        this.directionAfterNext = new DirectionData(-1, null);
    }
    private void checkMove() {
        if(!checkIfStandIsAlive()) return;
        if(!hasMovedBefore || nextDirection.getDirection() != -1) move();
        else setMoving(false);
        //if(checkifNewMoveIsNeeded()) move();
        //else setMoving(false);
    }

    private void move() {
        updateSpeed();

        //System.out.println("S: " + stand.getLocation());
        //System.out.println("N: " + nextLocation);
        if(checkIfStandIsAtLocation()) {
            startMovementCheck();
        }
        //if(checkIfStandIsAtLocation()) startMovementCheck();
        //startMovementCheck();
        tryToMoveStand();
        //tryToMoveStand();
    }

    private void startMovementCheck() {
        nextLocation.getBlock().setType(Material.GREEN_WOOL);
        updated = false;
        //if(!hasMovedBefore) {
        //    checkMovement();
        //}
        checkMovement();
        updateDirectionOrder();
    }
    private void updateDirectionOrder() {
        nextLocation = locationAfterNext.clone();
        currentDirection.updateData(nextDirection.getDirection(), nextDirection.getFactoryType());
        nextDirection.updateData(directionAfterNext.getDirection(), directionAfterNext.getFactoryType());
    }
    private void checkMovement() {
        for(int direction = 0; direction < 4; direction++) {
            if(checkIfMovementIsValid(direction)) return;
        }
    }
    private Boolean checkIfMovementIsValid(int direction) {
        Location checkingAt = locationAfterNext.clone().add(xIncrement[direction], 1.0, zIncrement[direction]);
        FactoryMaterials checkedMaterial = FactoryMaterials.checkAndGetFactoryMaterial(checkingAt.getBlock());
        return updateIfMaterialAndDirectionAreValid(checkedMaterial, direction);
    }
    private Boolean updateIfMaterialAndDirectionAreValid(FactoryMaterials material, int direction) {
        if(material == null || isDirectionOldDirection(direction, nextDirection.getDirection())) return false;
        updateDirection(material, direction);
        return true;
    }
    private void updateDirection(FactoryMaterials material, int direction) {
        updated = true;
        if(!hasMovedBefore) {
            nextDirection.updateData(direction, material);
            nextLocation.add(xIncrement[direction], 0.0, zIncrement[direction]);
            hasMovedBefore = true;
        }
        directionAfterNext.updateData(direction, material);
        locationAfterNext = locationAfterNext.clone().add(xIncrement[direction], 0.0, zIncrement[direction]);
    }

    private Boolean isDirectionOldDirection(int direction, int oldDirection) {
        oldDirection = oldDirection > 1 ? oldDirection - 2 : oldDirection+2;
        return direction == oldDirection;
    }

    /*private void checkMovement(Boolean checkingFirst) {
        if(!checkingFirst && hasMovedBefore) updateCurrentDirection();
        //if(!checkingFirst && hasMovedBefore) updateCurrentDirection();
        for(int checkingDirection = 0; checkingDirection < 4; checkingDirection++)
            if (checkLocation(checkingFirst, checkingDirection)) return;
    }
    private void updateCurrentDirection() {
        currentDirection.updateData(nextDirection.getDirection(), nextDirection.getFactoryType());
        nextDirection.updateData(secondDirection.getDirection(), secondDirection.getFactoryType());
    }

    private Boolean checkLocation(Boolean checkingFirst, int checkingDirection) {
        Location location = checkingFirst ? getCheckingLocation(checkingDirection) : getCheckingLocationSecond(checkingDirection);
        //for(int i = 0; i < 4; i++) {
        //    location.clone().add(xIncrement[i], 0.0, zIncrement[i]).getBlock().setType(Material.RED_WOOL);
        // }
        FactoryMaterials factoryPart = FactoryMaterials.checkAndGetFactoryMaterial(location.getBlock());
        return checkIfDirectionIsValidAndUpdate(factoryPart, checkingDirection, checkingFirst);
    }
    private Boolean checkIfDirectionIsValidAndUpdate(FactoryMaterials factoryPart, int checkingDirection, Boolean checkingFirst) {
        if(factoryPart == null ||
                !checkIfNextDirIsValid(checkingFirst ? currentDirection.getDirection() : nextDirection.getDirection(), checkingDirection)) {
            if(!checkingFirst) secondDirection.updateData(-1, null);
            else nextDirection.updateData(-1, null);
            return false;
        }
        if(checkingFirst) updateFirstDirection(checkingDirection, factoryPart);
        else updateSecondDirection(checkingDirection, factoryPart);
        //nextLocation.clone().add(0.0, -1.0, 0.0).getBlock().setType(Material.GREEN_WOOL);
        return true;
    }
    private void updateFirstDirection(int direction, FactoryMaterials factoryPart) {
        updated = true;
        currentDirection.updateData(direction, factoryPart);
        updateNextDirection();
    }
    private void updateSecondDirection(int direction, FactoryMaterials factoryPart) {
        updated = true;
        nextLocation.getBlock().setType(Material.GREEN_WOOL);
        secondDirection.updateData(direction, factoryPart);
        if(hasMovedBefore == true && (secondDirection.getFactoryType() == null || secondDirection.getFactoryType().getWaitingTime() > 0)) nextBlockWaiting = true;
        else if(nextBlockWaiting) nextBlockWaiting = false;

        if(hasMovedBefore) updateNextDirection();
        else hasMovedBefore = true;
    }
    private void updateNextDirection() {
        nextDirection.updateData(-1, null);
        nextLocation.add(xIncrement[currentDirection.getDirection()], 0.0, zIncrement[currentDirection.getDirection()]);
    }*/

    private void tryToMoveStand() {
        if(currentDirection.getDirection() != -1) {
            Location newStandLoc = stand.getLocation().add(xIncrement[currentDirection.getDirection()]*speed, 0.0, zIncrement[currentDirection.getDirection()]*speed);
            double xDifference = nextLocation.getX() - newStandLoc.getX();
            double zDifference = nextLocation.getZ() - newStandLoc.getZ();
            if((currentDirection.getDirection() == 0 || currentDirection.getDirection() == 2) && ((xDifference > 0 && xDifference < speed) || (xDifference < 0 && xDifference > -speed))) newStandLoc.add(xDifference, 0.0, 0.0);
            else if((zDifference > 0 && zDifference < speed) || (zDifference < 0 && zDifference > -speed)) newStandLoc.add(0.0, 0.0, zDifference);
            stand.moveStand(newStandLoc);
            //stand.moveStand(xIncrement[currentDirection.getDirection()]*speed, 0.0, zIncrement[currentDirection.getDirection()]*speed);
        }
        if(!updated) moving = false;
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
    private Boolean checkIfStandIsAtLocation() { return nextLocation != null && nextLocation.equals(stand.getLocation()); }
    private Location getCheckingLocation(int direction) { return stand.getStandBlockLocation().add(xIncrement[direction], 1.0, zIncrement[direction]); }
    private Location getCheckingLocationSecond(int direction) { return nextLocation == null ? null : nextLocation.clone().add(xIncrement[direction], 1.0, zIncrement[direction]); }

    private void setMoving(Boolean state) { moving = state; }
    private void updateSpeed() {
        if (nextBlockWaiting) {
            double difference = xIncrement[currentDirection.getDirection()] > 0.0 ? nextLocation.getX() - stand.getLocation().getX() : nextLocation.getZ() - stand.getLocation().getZ();
            double speedTest = speed;
            while(speedTest > 0.0) {
                difference -= speedTest;
                speedTest -= speedChange;
            }
            if(difference <= 0.0) speed -= speedChange;
        }
        else if(speed < maxSpeed) speed = (speed + speedChange) >= maxSpeed ? maxSpeed : speed + speedChange;
    }

    public void startMoving() {
        moving = true;
        new BukkitRunnable() {
            public void run() {
                if(moving) checkMove();
                else cancel();
            }
        }.runTaskTimer(Main.plugin, 4, 15);
    }

}
