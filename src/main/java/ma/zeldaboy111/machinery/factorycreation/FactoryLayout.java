package ma.zeldaboy111.machinery.factorycreation;

import ma.zeldaboy111.Main;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public enum FactoryLayout {
    STRAIGHT(new Material[] { null, Material.BLACK_CONCRETE, null,
            null, Material.BLACK_CONCRETE, null,
            null, Material.BLACK_CONCRETE, null, }),
    CORNER_L(new Material[] { null, Material.BLACK_CONCRETE, null,
            null, Material.BLACK_CONCRETE, Material.BLACK_CONCRETE,
            null, null, null, }),
    CORNER_R(new Material[] { null, Material.BLACK_CONCRETE, null,
            Material.BLACK_CONCRETE, Material.BLACK_CONCRETE, null,
            null, null, null, }),
    OAK_GENERATOR(new Material[] { null, Material.OAK_LOG, null,
            null, null, null,
            null, null, null, }),

    ;
    private Material[] layout;

    FactoryLayout(Material[] layout) {
        if(layout.length != 9) return;
        this.layout = layout;
    }
    public void build(Location location, int direction) {
        executeActionInArea(null, location, direction, true, false);
    }
    public void showParticles(Player p, Location loc, int direction) {
        new BukkitRunnable() {
            int runTime = 0;
            public void run() {
                if(p.isOnline()) executeActionInArea(p, loc, direction, false, true);
                else runTime = 5;
                runTime++;
                if(runTime > 4) { cancel(); return; }
            }
        }.runTaskTimer(Main.plugin, 0, 24);
    }
    private void executeActionInArea(Player p, Location loc, int direction, Boolean buildStructure, Boolean showParticles) {
        int id = 0, rows = 0;
        int[] checkX = getCheckX(direction);
        int[] checkZ = getCheckZ(direction);
        BuildQueue queue = buildStructure ? new BuildQueue() : null;
        Boolean rotate = checkX[0] != 0;
        for(int x : checkX) {
            for (int z : checkZ) {
                if (layout[id] != null) {
                    if (showParticles && p != null) showValidationParticles(p, loc.clone().add(x, 0, z).getBlock().getLocation());
                    if (buildStructure) {
                        queue.add(loc.clone().add(x, 0, z).getBlock().getLocation(), layout[id]);
                    }
                }
                id = rotate ? id + 3 : id + 1;
            }
            if (rotate) {
                rows++;
                id = rows;
            }
        }
        if(buildStructure) buildStructure(queue);
    }
    private void buildStructure(BuildQueue queue) {
        final FactoryLayout clazz = this;
        new BukkitRunnable() {
            int current = 0;
            public void run() {
                HashMap<Location, Material> target = queue.get(current);
                if(target == null) { cancel(); return; }
                try {
                    Location loc = (Location)target.keySet().toArray()[0];
                    loc.clone().add(0.5, 0.5, 0.5).getBlock().setType(target.get(loc));
                    showCreateParticles(loc.clone().add(0.5, 0.5, 0.5));
                    if(queue.get(current+1) == null) {
                        if (clazz == OAK_GENERATOR) FactoryManager.getInstance().addGenerator(loc, FactoryItem.OAK_GENERATOR.getGenerator());
                        cancel();
                        return;
                    }
                } catch(Exception e) { e.printStackTrace(); }
                current++;
            }
        }.runTaskTimer(Main.plugin, 0, 4);
    }

    private void showValidationParticles(Player p, Location loc) {
        if(loc.getBlock().getType() != Material.AIR) playInvalidEffect(p, loc);
        else playValidEffect(p, loc);
    }
    private void showCreateParticles(Location loc) {
        loc.getWorld().spawnParticle(Particle.CLOUD, loc.getBlock().getLocation().add(0.5, 0.5, 0.5), 4, 0.65, 0.65, 0.65, 0);
    }

    private void playValidEffect(Player p, Location loc) {
        p.spawnParticle(Particle.REDSTONE, loc.add(0.5, 0.5, 0.5), 2, 0, 0, 0, new Particle.DustOptions(Color.LIME, 2));
    }
    private void playInvalidEffect(Player p, Location loc) {
        p.spawnParticle(Particle.REDSTONE, loc.add(0.5, 0.5, 0.5), 2, 0, 0, 0, new Particle.DustOptions(Color.RED, 2));
    }

    public Boolean canBuild(int direction, Location check) {
        int id = 0, rows = 0;
        Boolean rotate = getCheckX(direction)[0] != 0;
        for(int x : getCheckX(direction)) {
            for(int z : getCheckZ(direction)) {
                if(layout[id] != null && check.clone().add(x, 0, z).getBlock().getType() != Material.AIR) return false;
                id = rotate ? id+3 : id+1;
            }
            if(rotate) {
                rows++;
                id = rows;
            }
        }
        return true;
    }
    public Location isStructure(Location check) {
        boolean directionInvalid = false;
        for(int direction = 0; direction < 4; direction++) {
            Boolean rotate = getCheckX(direction)[0] != 0;
            int id = 0, rows = 0;
            directionInvalid = false;
            for(int x : getCheckX(direction)) {
                if(!directionInvalid) {
                    for (int z : getCheckZ(direction)) {
                        if (layout[id] != null && check.clone().add(x, 0, z).getBlock().getType() != layout[id]) {
                            check.clone().add(x, 0, z).getBlock().setType(Material.RED_CONCRETE);
                            directionInvalid = true;
                        }
                        id = rotate ? id + 3 : id + 1;
                    }
                    if (rotate) {
                        rows++;
                        id = rows;
                    }
                    if(!directionInvalid){
                        return check.clone().add(getCheckX(direction)[direction == 1 || direction == 3 ? 1 : 0], 0, getCheckZ(direction)[direction == 0 || direction == 2 ? 1 : 0]);
                    }
                }
            }
        }
        return directionInvalid ? null : check;
    }

    private int[] getCheckX(int direction) {
        return direction == 1 ? new int[] { 0, 1, 2 } : direction == 3 ? new int[] { 0, -1, -2 } : direction == 2 ? new int[] { -1, 0, 1 } : new int[] { 1, 0, -1 };
    }
    private int[] getCheckZ(int direction) {
        return direction == 0 ? new int[] { 0, -1, -2 } : direction == 2 ? new int[] { 0, 1, 2 } : direction == 2 ? new int[] { -1, 0, 1 } : new int[] { 1, 0, -1 };
    }

    private class BuildQueue {
        private HashMap<Integer, HashMap<Location, Material>> queue;
        private BuildQueue() {
            queue = new HashMap<>();
        }
        public void add(Location loc, Material material) {
            HashMap<Location, Material> value = new HashMap<>();
            value.put(loc, material);
            queue.put(queue.size(), value);
        }
        public HashMap<Location, Material> get(int id) {
            if(id < 0 || id > queue.size()) return new HashMap<>();
            return queue.get(id);
        }

    }

}
