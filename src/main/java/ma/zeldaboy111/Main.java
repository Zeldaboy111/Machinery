package ma.zeldaboy111;

import ma.zeldaboy111.worldgeneration.ChunkGen;
import org.bukkit.Bukkit;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Plugin plugin;
    
    public static void main(String[] args) { }
    public void onEnable() {
        plugin = (Plugin)this;

        Bukkit.getServer().getPluginManager().registerEvents(new Listener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new WandPlayerListener(), this);
        Bukkit.getServer().getPluginCommand("factory").setExecutor(new Commands());
        Bukkit.getServer().getPluginCommand("wand").setExecutor(new Commands());
    }


    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new ChunkGen();
    }
    public void onDisable() { }

}
