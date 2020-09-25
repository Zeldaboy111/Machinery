package ma.zeldaboy111.FactoryHandler;

import org.bukkit.Location;

public class StandHandler {
    private StandHandler() { }

    public static void create(Location location) {
        new Stand(location);
    }
}
