package ma.zeldaboy111.machinery.factoryhandler;

import ma.zeldaboy111.machinery.FactoryMaterials;
import ma.zeldaboy111.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

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
    private Boolean waitedOnPrevious;

    private Location nextLocation;
    private Location locationAfterNext;

    private DirectionData currentDirection;
    private DirectionData nextDirection;
    private DirectionData directionAfterNext; // direction after 'nextDirection'

    private double speed;
    private int sleepTime;

    public Movement(Stand stand) {
        this.speed = 0.0;
        this.sleepTime = 0;
        this.stand = stand;
        this.hasMovedBefore = false;
        this.nextBlockWaiting = false;
        this.waitedOnPrevious = false;

        this.nextLocation = stand.getLocation();
        this.locationAfterNext = nextLocation.clone();

        this.currentDirection = new DirectionData(-1, null);
        this.nextDirection = new DirectionData(-1, null);
        this.directionAfterNext = new DirectionData(-1, null);
    }
    public void startMoving() {
        moving = true;
        new BukkitRunnable() {
            public void run() {
                if(moving) checkMove();
                else cancel();
            }
        }.runTaskTimer(Main.plugin, 3, 3);
    }

    private void checkMove() {
        if(!checkIfStandIsAlive()) return;
        if(checkIfStandCanMove()) move();
        else setMoving(false);
    }
    private Boolean checkIfStandIsAlive() {
        Boolean alive = stand != null && !stand.getStand().isDead();
        if (!alive) setMoving(false);
        return alive;
    }
    private Boolean checkIfStandCanMove() {
        return !hasMovedBefore || nextDirection.getDirection() != -1;
    }

    private void move() {
        updateSpeed();
        if(!nextBlockWaiting || waitedOnPrevious) {
            if(checkIfStandIsAtLocation()) startMovementCheck();
        }
        tryToMoveStand();
    }
    private void updateSpeed() {
        if (nextBlockWaiting && !waitedOnPrevious) {
            double difference = getRequiredDistance();
            if(difference <= 0.0) speed -= speedChange;
            if(speed <= 0.0) {
                speed = 0.0;
                sleep();
            }
        } else {
            if(speed < maxSpeed) speed = (speed + speedChange) >= maxSpeed ? maxSpeed : speed + speedChange;
            updateWaitedOnPrevious();
        }
        speed = Math.round(speed*100.0)/100.0;
    }
    private double getRequiredDistance() {
        double distance = xIncrement[currentDirection.getDirection()] > 0.0 ? nextLocation.getX() - stand.getLocation().getX() : nextLocation.getZ() - stand.getLocation().getZ();
        double speedTest = speed;
        while(speedTest > 0.0) {
            distance -= speedTest;
            speedTest -= speedChange;
        }
        return distance;
    }
    private void collectItem() {
        stand.collect();
        currentDirection.updateData(-1, null);
        moving = false;
    }
    private void sleep() {
        sleepTime += 1;
        if(sleepTime > directionAfterNext.getFactoryType().getWaitingTime()) {
            if(nextDirection.getFactoryType() == FactoryMaterials.COLLECTOR) { collectItem(); return; }

            stand.updateItem(nextDirection.getFactoryType().getOutcome());
            startMovementCheck();
            updateDirectionOrder();
            checkIfStandHasToWaitAtNextLocation();
            waitedOnPrevious = true;
            sleepTime = 0;
        }
    }
    private void updateWaitedOnPrevious() {
        if(waitedOnPrevious && !nextBlockWaiting) waitedOnPrevious = false;
        else if(nextBlockWaiting && waitedOnPrevious) {
            double distance = xIncrement[currentDirection.getDirection()] > 0.0 ? nextLocation.getX() - stand.getLocation().getX() : nextLocation.getZ() - stand.getLocation().getZ();
            if(distance - speed > 0.0) waitedOnPrevious = false;
        }
    }

    private Boolean checkIfStandIsAtLocation() {
        return nextLocation != null && nextLocation.equals(stand.getLocation());
    }

    /**
     *  Movement check
     *   Checks for a next location to move the armor stand to
      */
    private void startMovementCheck() {
        updated = false;
        checkMovement();
        updateDirectionOrder();
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
        checkIfStandHasToWaitAtNextLocation();
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
    private void checkIfStandHasToWaitAtNextLocation() {
        nextBlockWaiting = false;
        FactoryMaterials material = directionAfterNext.getFactoryType();
        if(material.getWaitingTime() > 0 &&
                (material.getInput() == Material.AIR || stand.getStand().getHelmet().getType() == material.getInput())) {
            nextBlockWaiting = true;
        }
    }

    private void updateDirectionOrder() {
        nextLocation = locationAfterNext.clone();
        nextDirection.updateData(directionAfterNext);
        currentDirection.updateData(nextDirection);
    }

    /**
     * Movement handlers
     *  Moves the armor stand to the new location
     */
    private void tryToMoveStand() {
        if(sleepTime == 0 && currentDirection.getDirection() != -1)stand.moveStand(trySnapStandIntoPosition());
        if(!updated) moving = false;
    }
    private Location trySnapStandIntoPosition() {
        Location newStandLoc = stand.getLocation().add(xIncrement[currentDirection.getDirection()]*speed, 0.0, zIncrement[currentDirection.getDirection()]*speed);
        double xDifference = nextLocation.getX() - newStandLoc.getX();
        double zDifference = nextLocation.getZ() - newStandLoc.getZ();
        if((currentDirection.getDirection() == 0 || currentDirection.getDirection() == 2) && ((xDifference > 0 && xDifference < speed) || (xDifference < 0 && xDifference > -speed))) newStandLoc.add(xDifference, 0.0, 0.0);
        else if((zDifference > 0 && zDifference < speed) || (zDifference < 0 && zDifference > -speed)) newStandLoc.add(0.0, 0.0, zDifference);
        return newStandLoc;
    }

    private void setMoving(Boolean state) { moving = state; }

}
