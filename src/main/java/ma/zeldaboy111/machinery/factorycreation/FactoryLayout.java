package ma.zeldaboy111.machinery.factorycreation;

import ma.zeldaboy111.Main;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public enum FactoryLayout {
    STRAIGHT(new Material[] { null, Material.BLACK_CONCRETE, null,
            null, Material.BLACK_CONCRETE, null,
            null, Material.BLACK_CONCRETE, null, }),
    CORNER(new Material[] { null, Material.BLACK_CONCRETE, null,
            null, Material.BLACK_CONCRETE, Material.BLACK_CONCRETE,
            null, null, null, }),

    ;
    private Material[] layout;

    FactoryLayout(Material[] layout) {
        if(layout.length != 9) return;
        this.layout = layout;
    }
    public void build(float yaw, Location start, int direction) {
        int id = 0, rows = 0;
        int[] checkX = getCheckX(yaw);
        int[] checkZ = getCheckZ(yaw);
        Boolean rotate = checkX[0] != 0;
        Boolean rotateX = yaw >= 45 && yaw < 135, rotateZ = yaw >= 135 && yaw < 225;
        for(int x : checkX) {
            for(int z : checkZ) {
                if(layout[id] != null) start.clone().add(rotateX ? -x : x, 0, rotateZ ? -z : z).getBlock().setType(layout[id]);
                id = rotate ? id+3 : id+1;
            }
            if(rotate) {
                rows++;
                id = rows;
            }
        }
    }
    public void showParticles(Player p, Location start, int direction) {
        final float yaw = p.getLocation().getYaw();
        new BukkitRunnable() {
            int runTime = 0;
            public void run() {
                if(p.isOnline()) showParticle(p, yaw, start);
                else runTime = 5;
                runTime++;
                if(runTime > 4) { cancel(); return; }
            }
        }.runTaskTimer(Main.plugin, 0, 23);
    }
    private void showParticle(Player p, float yaw, Location start) {
        int id = 0, rows = 0;
        int[] checkX = getCheckX(yaw);
        int[] checkZ = getCheckZ(yaw);
        Boolean rotate = checkX[0] != 0;
        Boolean rotateX = yaw >= 45 && yaw < 135, rotateZ = yaw >= 135 && yaw < 225;
        for(int x : checkX) {
            for(int z : checkZ) {
                if(layout[id] != null) {
                    Location loc = start.clone().add(rotateX ? -x : x, 0, rotateZ ? -z : z).getBlock().getLocation();
                    if(loc.getBlock().getType() != Material.AIR) playInvalidEffect(p, loc);
                    else playValidEffect(p, loc);
                }
                id = rotate ? id+3 : id+1;
            }
            if(rotate) {
                rows++;
                id = rows;
            }
        }
    }

    private void playValidEffect(Player p, Location loc) {
        p.spawnParticle(Particle.REDSTONE, loc.add(0.5, 0.5, 0.5), 2, 0.05, 0.05, 0.05, new Particle.DustOptions(Color.LIME, 2));
    }
    private void playInvalidEffect(Player p, Location loc) {
        p.spawnParticle(Particle.REDSTONE, loc.add(0.5, 0.5, 0.5), 2, 0.05, 0.05, 0.05, new Particle.DustOptions(Color.RED, 2));
    }

    public Boolean canBuild(float yaw, Location check) {
        int id = 0;
        for(int x : getCheckX(yaw)) {
            for(int z : getCheckZ(yaw)) {
                if(layout[id] != null && check.clone().add(x, 0, z).getBlock().getType() != Material.AIR) return false;
                id++;
            }
        }
        return true;
    }

    private int[] getCheckX(float yaw) {
        return (yaw >= 315 || yaw < 45) || (yaw >= 135 && yaw < 225) ? new int[] { -1, 0, 1 } : new int[] { 0, 1, 2 };
    }
    private int[] getCheckZ(float yaw) {
        return (yaw >= 45 || yaw < 135) || (yaw >= 225 && yaw < 315) ? new int[] { 0, 1, 2 } : new int[] { -1, 0, 1 };
    }


}
