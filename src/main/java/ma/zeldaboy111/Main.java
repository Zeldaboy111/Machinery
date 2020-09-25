package ma.zeldaboy111;

import ma.zeldaboy111.FactoryHandler.StandHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Plugin plugin;

    public void onEnable() {
        plugin = (Plugin)this;
        Bukkit.getServer().getPluginCommand("factory").setExecutor(new Commands());

    }

    public void onDisable() { }

}
