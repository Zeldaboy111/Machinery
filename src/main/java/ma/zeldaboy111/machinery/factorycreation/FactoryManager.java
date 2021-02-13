package ma.zeldaboy111.machinery.factorycreation;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;

public class FactoryManager {
    private static FactoryManager clazz;
    private FactoryManager() {}
    public static FactoryManager getInstance() { return clazz == null ? (clazz = new FactoryManager()) : clazz; }
    private static final HashMap<Location, Generator> generators = new HashMap<>();

    public void addGenerator(Location loc, FactoryGenerator generator) {
        loc.getBlock().setType(Material.LIME_WOOL);
        generators.put(loc, new Generator(loc, generator));
    }
    public void removeGenerator(Location loc) {
        loc.getBlock().setType(Material.RED_WOOL);
        if(generators.get(loc.getBlock().getLocation()) == null) return;
        generators.get(loc.getBlock().getLocation()).shutdown();
        generators.remove(loc.getBlock().getLocation());
    }

}
