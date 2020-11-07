package ma.zeldaboy111;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Plugin plugin;
    
    public static void main(String[] args) { }
    public void onEnable() {
        plugin = (Plugin)this;

        Bukkit.getServer().getPluginManager().registerEvents(new Listener(), this);
        Bukkit.getServer().getPluginCommand("factory").setExecutor(new Commands());
    }

    public void onDisable() { }

}
