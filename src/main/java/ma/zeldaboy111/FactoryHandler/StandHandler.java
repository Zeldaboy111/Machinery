package ma.zeldaboy111.FactoryHandler;

import ma.zeldaboy111.FactoryMaterials;
import ma.zeldaboy111.Main;
import net.minecraft.server.v1_15_R1.Packet;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class StandHandler {
    public static final StandHandler instance = new StandHandler();
    private final ArrayList<Stand> standList = new ArrayList<>();

    /**
     * @param loc the starting location of the armor stand
     * @return the instance of the Stand-class that has been created
     */
    public Stand newStand(Location loc) {
        Stand stand = new Stand(loc);
        standList.add(stand);
        return stand;
    }



    private class Stand {
        boolean moving;
        boolean initFirst; // If true, initialize the first location. If false, only initialize the second

        double[] xIncrement;
        double[] zIncrement;
        double speed; // Current stand's speed
        double maxSpeed; // Maximum reachable speed

        int lastDetectedDirection; // The last detected direction

        DirectionData dir; // Current direction from the stand
        DirectionData nextDir; // Next direction from the stand
        DirectionData nextDir2; // Next direction after nextDir from the stand

        Location loc;
        Location nextLoc;
        ArmorStand stand;

        public Stand(Location loc) {
            this.loc = loc.getBlock().getLocation().add(0.5, -1.43, 0.5);
            this.nextLoc = this.loc.clone().getBlock().getLocation();
            this.stand = (ArmorStand) this.loc.getWorld().spawnEntity(this.loc, EntityType.ARMOR_STAND);

            speed = 0.25;
            maxSpeed = 0.25;

            xIncrement = new double[]{1.0, 0.0, -1.0, 0.0};
            zIncrement = new double[]{0.0, 1.0, 0.0, -1.0};

            dir = new DirectionData(-1, null);
            nextDir = new DirectionData(-1, null);
            nextDir2 = new DirectionData(-1, null);
            initFirst = true;

            stand.setHelmet(new ItemStack(Material.OAK_LOG));
            stand.setGravity(false);
            stand.setVisible(false);
            movementHandler();
        }

        public void remove() {
            moving = false;
            standList.remove(this);

        }

        private void movementHandler() {
            moving = true;

            new BukkitRunnable() {
                public void run() {
                    if (moving) {
                        move();
                    } else {
                        cancel();
                        remove();
                        return;
                    }
                }
            }.runTaskTimer(Main.plugin, 2, 3);
        }

        /**
         * Handles the movement of the stand
         */
        private void move() {
            if (!testAlive()) return;

            if (nextDir == null || nextDir.getDirection() == -1) {
                if (nextLoc != null && nextLoc.equals(stand.getLocation().getBlock().getLocation())) {
                    boolean updated = false;
                    for (int i = 0; i < 4; i++) {
                        Location next = nextLoc.clone().add(xIncrement[i], 1.0, zIncrement[i]);

                        if (initFirst) {
                            FactoryMaterials part = isNextFactoryPart(next);
                            if (part != null && isNextDirValid(i, dir.getDirection())) {
                                if (nextDir == null) nextDir = new DirectionData(i, part);
                                else nextDir.updateData(i, part);
                                initFirst = false;
                                updated = true;
                            }
                        } else {
                            /*if(nextDir2 != null) {
                                if(nextDir == null) nextDir = new DirectionData(nextDir2.getDirection(), nextDir2.getFactoryType());
                                else nextDir.updateData(nextDir2.getDirection(), nextDir2.getFactoryType());
                            }
                            else nextDir = null;*/
                            if (nextDir2 != null) {
                                nextDir.updateData(nextDir2.getDirection(), nextDir2.getFactoryType());
                            }

                            Location next1 = next.clone().add(xIncrement[i], 0.0, zIncrement[i]);
                            FactoryMaterials part = isNextFactoryPart(next1);
                            if (part != null && nextDir != null && isNextDirValid(i, nextDir.getDirection()) && (lastDetectedDirection == -1 || isNextDirValid(i, lastDetectedDirection))) {
                                if (nextDir2 == null) nextDir2 = new DirectionData(i, part);
                                else nextDir2.updateData(i, part);
                                lastDetectedDirection = i;
                                updated = true;
                            } else {
                                nextDir2.updateData(-1, null);
                                //System.out.println(nextDir.getDirection());
                                if(nextDir.getDirection() != -1) updated = true;
                            }



                            /*FactoryMaterials part = isNextFactoryPart(next);
                            if(part != null && isNextDirValid(i)) {
                                if(nextDir == null) nextDir = new DirectionData(i, part);
                                else nextDir.updateData(i, part);
                                updated = true;
                            }*/
                        }

                        /* else {
                            if(nextDir2 != null) {
                                if(nextDir == null) nextDir = new DirectionData(nextDir2.getDirection(), nextDir2.getFactoryType());
                                else nextDir.updateData(nextDir2.getDirection(), nextDir2.getFactoryType());
                            }
                            else nextDir = null;
                        }*/


                        /*FactoryMaterials part = isNextFactoryPart(next1);
                        System.out.println(i + ": direction2: " + part + " " + isNextDirValid(i, nextDir.getDirection()));
                        if(part != null && nextDir != null && isNextDirValid(i, nextDir.getDirection())) {
                            if(nextDir2 == null) nextDir2 = new DirectionData(i, part);
                            else nextDir2.updateData(i, part);
                            System.out.println("UPDATED SECOND DIRECTION");
                            if(!isFirstDetection) updated = true;
                        }*/


                        if (updated) {
                            if (dir == null) dir = new DirectionData(nextDir.getDirection(), nextDir.getFactoryType());
                            else dir.updateData(nextDir.getDirection(), nextDir.getFactoryType());

                            if (dir.getDirection() != -1) {
                                nextDir.updateData(-1, null);
                                nextLoc.add(xIncrement[dir.getDirection()], 0.0, zIncrement[dir.getDirection()]);
                            }
                            break;
                        }
                    }

                    if (!updated) {
                        dir.updateData(-1, null);
                        moving = false;
                        System.out.println("[Factory] Found no new belt, force-shutting off...");
                    }

                }
            } else {
                moving = false;
                return;
            }

            if (dir != null && dir.getDirection() != -1) {
                stand.teleport(stand.getLocation().clone().add(xIncrement[dir.getDirection()], 0.0, zIncrement[dir.getDirection()]));
            }

        }

        private Boolean testAlive() {
            Boolean alive = stand != null && !stand.isDead();
            if (!alive) moving = false;
            return alive;
        }

        private FactoryMaterials isNextFactoryPart(Location loc) {
            Material mat = loc.getBlock().getType();
            //System.out.println(mat);
            if (mat == FactoryMaterials.BELT.getMaterial()) return FactoryMaterials.BELT;
            //else loc.getBlock().setType(Material.RED_WOOL);
            return null;
        }

        private boolean isNextDirValid(int i, int dir) {
            int old = i > 1 ? i - 2 : i + 2;
            return old != dir;
        }

        /*private double getIncrement(int dir) {
            if(isZ) {
                int dir1 = dir;
                if(dir >= 3) dir1 = 0;
                else dir1++;

                return increment[dir1];
            } else {
                return increment[dir];
            }
        }*/
    }

}
