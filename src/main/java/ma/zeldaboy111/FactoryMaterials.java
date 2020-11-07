package ma.zeldaboy111;

import org.bukkit.Material;
import org.bukkit.block.Block;

public enum FactoryMaterials {
    BELT (Material.BLACK_CONCRETE, 0),
    COLLECTOR(Material.HOPPER, 1),
    STRIP_BLOCK (Material.STRIPPED_OAK_LOG, 10, Material.OAK_LOG, Material.STRIPPED_OAK_LOG),
    STRIP_BLOCK1 (Material.STRIPPED_DARK_OAK_LOG, 10, Material.DARK_OAK_LOG, Material.STRIPPED_DARK_OAK_LOG),
    SAWMILL(Material.OAK_PLANKS, 8, Material.STRIPPED_OAK_LOG, Material.OAK_PLANKS),
    ;

    private int waitingTime;
    private Material mat;
    private Material input;
    private Material outcome;
    FactoryMaterials(Material material, int waitingTime) {
        this.mat = material;
        this.waitingTime = waitingTime;
        this.input = Material.AIR;
        this.outcome = Material.AIR;
    }
    FactoryMaterials(Material material, int waitingTime, Material outcome) {
        this.mat = material;
        this.waitingTime = waitingTime;
        this.input = Material.AIR;
        this.outcome = outcome;
    }
    FactoryMaterials(Material material, int waitingTime, Material input, Material outcome) {
        this.mat = material;
        this.waitingTime = waitingTime;
        this.input = input;
        this.outcome = outcome;
    }


    public Material getInput() { return input; }
    public Material getOutcome() { return outcome; }

    public Material getMaterial() {
        return mat;
    }
    public int getWaitingTime() { return waitingTime; }

    public static FactoryMaterials checkAndGetFactoryMaterial(Block block) {
        Material material = block.getType();
        if(material == Material.AIR) return null;
        for(FactoryMaterials factoryMaterial : FactoryMaterials.values())
            if(factoryMaterial.getMaterial() == material) return factoryMaterial;
        return null;
    }
}
