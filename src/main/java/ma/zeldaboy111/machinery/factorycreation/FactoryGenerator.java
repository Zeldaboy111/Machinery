package ma.zeldaboy111.machinery.factorycreation;

import org.bukkit.Material;

public enum FactoryGenerator {
    OAK_WOOD(40, Material.OAK_WOOD),

    ;

    private int delay;
    private Material spawnMaterial;
    FactoryGenerator(int delay, Material spawnMaterial) {
        this.delay = delay;
        this.spawnMaterial = spawnMaterial;
    }
    public int getDelay() { return delay; }
    public Material getSpawnMaterial() { return spawnMaterial; }

}
