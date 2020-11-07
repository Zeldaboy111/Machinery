package ma.zeldaboy111;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;

public enum FactoryMaterials {
    BELT (Material.BLACK_CONCRETE, 0),
    STRIP_BLOCK (Material.STRIPPED_OAK_LOG, 20),
    ;

    private int waitingTime;
    private Material mat;
    FactoryMaterials(Material material, int waitingTime) {
        this.mat = material;
        this.waitingTime = waitingTime;
    }

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
