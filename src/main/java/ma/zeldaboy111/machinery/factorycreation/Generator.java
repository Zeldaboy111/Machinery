package ma.zeldaboy111.machinery.factorycreation;

import ma.zeldaboy111.Main;
import ma.zeldaboy111.machinery.factoryhandler.StandHandler;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class Generator {

    private Location spawnLoc;
    private FactoryGenerator parent;
    private Boolean stop;
    public Generator(Location location, FactoryGenerator parent) {
        this.spawnLoc = location.clone().add(0.0, 1.0, 0.0);
        this.parent = parent;
        this.stop = false;
        spawn();
    }
    private void spawn() {
        new BukkitRunnable() {
            public void run() {
                if(stop) { cancel(); return; }
                StandHandler.create(spawnLoc);
            }
        }.runTaskTimer(Main.plugin, parent.getDelay(), parent.getDelay());
    }
    public void shutdown() { stop = true; }

}
