package ma.zeldaboy111.machinery.factoryhandler;

import org.bukkit.Location;

public class StandHandler {
    private StandHandler() { }

    public static void create(Location location) {
        new Stand(location);
    }
}
