package ma.zeldaboy111.FactoryHandler;

import ma.zeldaboy111.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Factory {
    public static final Material beltBlock = Material.BLACK_CONCRETE;
    public static final Material stripBlock = Material.STRIPPED_OAK_LOG;
    public static final Material pickupBlock = Material.HOPPER;
    public static final List<Material> factoryMaterials = new ArrayList<Material>();

    final Boolean verbose = true;

    Material currentMaterial, nextMaterial;
    int delay;
    int dir, nextDir, nextDir2;
    double xInc[], zInc[], speed, speedDifference, currentSpeed, slowdownSpeed;
    double strippedTicks;
    Boolean initFirst;
    Boolean moving, slow, slowForAction, dropAtNext;
    Boolean stripped;
    Location locFull, loc, nextLoc;
    ArmorStand stand;

     /*
    public static final Material beltBlock = Material.BLACK_CONCRETE;
    public static final Material stripBlock = Material.STRIPPED_OAK_LOG;
    public static final Material pickupBlock = Material.HOPPER;
    public static final List<Material> factoryMaterials = new ArrayList<Material>();
    private final boolean verbose = true;

    Material startMaterial, currentMaterial;

    int delay, dir, nextDir, nextDir2;
    double xInc[], zInc[], speed, speedDifference, currentSpeed, slowdownSpeed, strippedTicks;

    boolean initFirst, moving, dropAtNext, slow, slowForAction, stripped;
    Location currentBlockLock, loc, action;
    ArmorStand stand;

         */

    public Factory(Location loc, Material startMaterial) {
        speed = 0.19;
        speedDifference = 0.02;
        slowdownSpeed = 0.05;
        delay = 1;

        nextDir = -1;
        nextDir2 = -1;
        dir = -1;

        strippedTicks = 40;

        initFirst = true;

        xInc = new double[] { 1.0, 0.0, -1.0, 0.0 };
        zInc = new double[] { 0.0, 1.0, 0.0, -1.0 };

        this.loc = loc.clone().getBlock().getLocation().add(0.5, -1.43, 0.5);
        //this.startMaterial = startMaterial;
        //this.currentMaterial = this.startMaterial;

        stand = (ArmorStand)this.loc.getWorld().spawnEntity(this.loc, EntityType.ARMOR_STAND);
        stand.setHelmet(new ItemStack(Material.OAK_LOG));
        stand.setGravity(false);
        stand.setVisible(false);

        locFull = stand.getLocation().getBlock().getLocation();
        nextLoc = locFull;

        //currentBlockLock = stand.getLocation().clone().add(0.0, 1.0, 0.0);
        //nextBlockLoc = new Location[4];

        //nextBlockLoc[0] = this.loc.getBlock().getLocation().add(1.0, 1.0, 0.0);
        //nextBlockLoc[1] = this.loc.getBlock().getLocation().add(0.0, 1.0, 1.0);
        //nextBlockLoc[2] = this.loc.getBlock().getLocation().add(-1.0, 1.0, 0.0);
        //nextBlockLoc[3] = this.loc.getBlock().getLocation().add(0.0, 1.0, -1.0);

        startMoving();
    }

    public void startMoving() {
        moving = true;

        new BukkitRunnable() {
            public void run() {
                /*if(!moving) {
                    cancel();
                    stand.remove();
                }
                else move();*/
                if(moving) move();
            }
        }.runTaskTimer(Main.plugin, 0, delay);
    }

    private void move() {
        if(!isAlive()) return;
        //stand.teleport(stand.getLocation().clone().add(0.1, 0.0, 0.0));

        if(nextDir == -1) {
            if(nextLoc != null && nextLoc.equals(stand.getLocation().getBlock().getLocation())) {
                boolean updated = false;
                for(int i = 0; i < 4; i++) {
                    Location nextLoc = stand.getLocation().getBlock().getLocation().clone().add(xInc[i], 1.0, zInc[i]);
                    Location nextLoc2 = nextLoc.clone().add(xInc[i], 1.0, zInc[i]);
                    if(initFirst) {
                        System.out.println(nextDirValid(nextLoc, i));
                        if(factoryMaterials.contains(nextLoc.getBlock().getType()) && nextDirValid(nextLoc, i)) {
                            nextDir = i;
                            initFirst = false;
                            updated = true;
                        }
                    } else {
                        nextDir = nextDir2;
                    }

                    if(factoryMaterials.contains(nextLoc.getBlock().getType()) && nextDirValid(nextLoc2, i)) {
                        nextDir2 = i;
                        updated = true;
                    }

                    if(updated) {
                        dir = nextDir;
                        if(verbose) {
                            System.out.println("[Factory] Updated directions (" + dir + ", " + nextDir2 + ").");
                        }

                        if(dir != -1) {
                            nextDir = -1;
                            this.nextLoc.add(xInc[dir], 0.0, zInc[dir]);
                        }
                        break;
                    }

                    /*Location nextLoc2 = nextLoc.clone().add(xInc[i], 1.0, zInc[i]);
                    if(initFirst) {
                        if(factoryMaterials.contains(nextLoc.getBlock().getType())) {
                            nextDir = getNextDir(nextLoc, i);
                            initFirst = false;
                        }
                    } else {
                        nextDir = nextDir2;
                    }
                    nextDir2 = getNextDir(nextLoc2, i);

                    this.nextLoc = nextLoc.add(0.0, -1.0, 0.0);
                    dir = nextDir;

                    if(verbose) {
                        System.out.println("[Factory] Updated the directions (" + dir + ", " + nextDir2 + ").");
                    }*/
                    //if(dir != nextDir) break;
                    /**
                     Nextdir moet gezet worden indien:
                     -> directie veranderd
                     -> nieuwe stand

                     **/


                /*Location nextLoc = currentBlockLock.clone().add(new Vector(xInc[i], 0.0, zInc[i]));
                Location nextLoc2 = currentBlockLock.clone().add(new Vector(xInc[i]*2, 0.0, zInc[i]*2));
                if(factoryMaterials.contains(nextLoc.getBlock().getType())) {
                    getNextDir(nextLoc, i);
                    if(dir != nextDir) break;
                }
                if(factoryMaterials.contains(nextLoc2.getBlock().getType())) {
                    getNextDir(nextLoc2, i);
                    if(dir != nextDir) break;
                }*/
                }
                if(!updated) {
                    dir = -1;
                    System.out.println("[Factory] Found no new belt, force-shutting off...");
                }
            }
        } else {
            moving = false;
            return;
        }

        //dir = nextDir;
        //nextDir = -1;
        //currentSpeed = speed;
        //double x = xInc[dir]*currentSpeed;
        //double z = zInc[dir]*currentSpeed;
        if(dir != -1) {
            double x = xInc[dir];
            double z = zInc[dir];

            stand.teleport(stand.getLocation().clone().add(x, 0.0, z));
        }

        /*Boolean slowing_down = false;
        if(nextDir != dir) {
            slowing_down = true;
        }

        //currentSpeed = speed;

        if(checkDir(currentBlockLock.getBlockX(), stand.getLocation().getX()) && checkDir(currentBlockLock.getBlockZ(), stand.getLocation().getZ())) {
            //if(dir != nextDir) Bukkit.getServer().getConsoleSender().sendMessage("DIRECTION CHANGED");

            if(nextDir != -1) {
                currentBlockLock.add(xInc[nextDir], 0.0, zInc[nextDir]);
            } else {
                dir = -1;
                moving = false;
                return;
            }
            dir = nextDir;
            nextDir = -1;
        }

        if(slowing_down) getSlowingSpeed(0.1);
        else {
            getCurrentSpeed();
        }
        double x = xInc[dir]*currentSpeed;
        double z = zInc[dir]*currentSpeed;

        stand.teleport(stand.getLocation().clone().add(x, 0.0, z));*/
    }

    private boolean checkDir(int checkLoc, Double standLoc) {
        if(dir == 3 || dir == 4) {
            if(checkLoc-standLoc > 0) {
                return checkLoc-standLoc <= 1.0 && checkLoc-standLoc >= 0.5;
            } else {
                return checkLoc-standLoc >= -1.0 && checkLoc-standLoc >= -0.5;
            }
        }
        if(standLoc-checkLoc > 0) {
            return standLoc-checkLoc <= 1.0 && standLoc-checkLoc >= 0.5;
        } else {
            return standLoc-checkLoc <= -0.5+currentSpeed && standLoc-checkLoc >= -0.5;
        }
    }

    private boolean nextBlockAir(double x, double y, double z, boolean dropAtNext) {
        if(currentSpeed <= 0.1) return true;
        return false;
    }

    private void getCurrentSpeed() {
        if(currentSpeed+slowdownSpeed >= speed) currentSpeed = speed;
        else currentSpeed += slowdownSpeed;
    }

    /**
     * @param limit is the slowest a factory-stand may move
     */

    private void getSlowingSpeed(double limit) {
        double difference = -1;

        if(currentSpeed-slowdownSpeed <= limit) currentSpeed = limit;
        else currentSpeed -= slowdownSpeed;

        //VISUAL BUG????
        if(dir == 0 || dir == 2) difference = stand.getLocation().getX()-stand.getLocation().getBlockX();
        else if(dir != -1) difference = stand.getLocation().getY() - stand.getLocation().getBlockY();

        //System.out.println(dir + ": " + difference);
    }

    /**
     *
     * @param loc location that is being checked
     * @param i is the direction that is being checked
     * @return returns the next direction, `-1` means the old direction is opposite of the last direction
     */
    private boolean nextDirValid(Location loc, int i) {
        /*int checkOldDir = i > 1 ? i-2 : i+2;
                    if(oldDir != checkOldDir) {
                        nextDir = i;

                        //for(int j = 0; j < 4; j++) {
                        //    nextBlockLoc[j] = nextLoc.clone().add(xInc[j], 0.0, zInc[j]);
                        //}

                        break;
                    } else {
                        nextDir = -1;
                    }
                }*/

        int old = i > 1 ? i - 2 : i + 2;
        return old != dir;
    }

    private Boolean isAlive() {
        boolean alive = stand != null && !stand.isDead();
        if(!alive) moving = false;

        return alive;
    }

    /*private void getSlowingSpeed(double limit) {
        if(currentSpeed-slowdownSpeed*2 <= limit) currentSpeed = limit;
        else currentSpeed -= slowdownSpeed*2;
        double difference = -1;
        if(dir == 0 || dir == 2) {
            difference = stand.getLocation().getX()-stand.getLocation().getBlockX();

        } else if(dir != -1) {
            difference = stand.getLocation().getY()-stand.getLocation().getBlockY();
        }

        if(difference < 0 && difference+slowdownSpeed > -0.5) currentSpeed = -difference;
        else if(difference-slowdownSpeed > 0.5) currentSpeed = difference;
    }*/


    /*private void move() {
        if(stand == null || stand.isDead()) moving = false;
        if(slow == true && (stand.getLocation().getX() - stand.getLocation().getBlockX() >= 0.5 || stand.getLocation().getX() - stand.getLocation().getBlockX() <= -0.5)) moving = false;
        if(dropAtNext && (currentSpeed <= 0.1 || (stand.getLocation().getX() - action.getX() >= 0.5 && stand.getLocation().getX() - action.getX() < 1.0))) {
            moving = false;
            Location loc = stand.getLocation();
            loc.getWorld().dropItem(loc.clone().add(0.0, 1.5, 0.0), new ItemStack(currentMaterial, 1)).setVelocity(new Vector(0.0, 0.0, 0.0));
            stand.remove();
            return;
        }
        if(slowForAction == true && ((stand.getLocation().getX() - action.getX() >= 0.5 && stand.getLocation().getX() - action.getX() < 1.0))) {
            if(stripped == false && strippedTicks >= 40) strippedTicks = 0;
            strippedTicks += delay;
            if(strippedTicks >= 40) {
                stripped = true;
                slowForAction = false;
                stand.setHelmet(new ItemStack(Material.STRIPPED_OAK_LOG));
                currentMaterial = Material.STRIPPED_OAK_LOG;
            }
        }

        if(moving == false) return;

        Location sl = stand.getLocation().getBlock().getLocation().add(0.0, 1.0, 0.0);
        if((useSecondNext == false && (currentBlockLock.getBlockX() != sl.getBlockX() || currentBlockLock.getY() != sl.getY() || currentBlockLock.getZ() != sl.getZ())) || (useSecondNext && (currentBlockLock.getBlockX() != sl.getBlockX() || currentBlockLock.getY() != sl.getY() || currentBlockLock.getZ() != sl.getZ()))) {
            currentBlockLock = nextBlockLoc[0].clone();
            nextBlockLoc[0] = nextBlockLoc[0].add(1.0, 0.0, 0.0);

            Material nextBlock = currentBlockLock.clone().add(1.0, 0.0, 0.0).getBlock().getType();
            Material nextNextBlock = currentBlockLock.clone().add(2.0, 0.0, 0.0).getBlock().getType();
            if(useSecondNext) useSecondNext = false;
            else useSecondNext = true;

            if(nextBlock != beltBlock || nextNextBlock != beltBlock) {
                if(nextBlock == stripBlock) {
                    if(!stripped) {
                        slowForAction = true;
                        action = currentBlockLock.clone().add(1.0, 0.0, 0.0);
                    }
                } else if(nextBlock == pickup) {
                    dropAtNext = true;
                    action = currentBlockLock.clone().add(1.0, 0.0, 0.0);
                } else {
                    if(nextNextBlock == stripBlock || nextNextBlock == pickup);
                    else slow = true;
                }
            }
        }

        xInc = getCurrentSpeed();
        if(slow || slowForAction || dropAtNext) xInc = getSlowingSpeed();
        if(strippedTicks >= 40 && moving) stand.teleport(stand.getLocation().add(new Vector(xInc, yInc, zInc)));
    }*/

}
