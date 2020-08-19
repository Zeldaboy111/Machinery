package ma.zeldaboy111;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class Main {
    public static Plugin plugin;

    public void onEnable() {
        plugin = (Plugin)this;
        Bukkit.getServer().getPluginCommand("factory").setExecutor(new Commands());

    }

    public void onDisable() {

    }

}
