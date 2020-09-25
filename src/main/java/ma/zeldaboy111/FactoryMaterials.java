package ma.zeldaboy111;

import org.bukkit.Material;

public enum FactoryMaterials {
    BELT (Material.BLACK_CONCRETE);


    Material mat;
    FactoryMaterials(Material material) {
        this.mat = material;
    }

    public Material getMaterial() {
        return mat;
    }

}
