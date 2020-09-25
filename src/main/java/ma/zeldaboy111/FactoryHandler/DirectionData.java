package ma.zeldaboy111.FactoryHandler;

import ma.zeldaboy111.FactoryMaterials;

public class DirectionData {

    private int direction;
    private FactoryMaterials factoryType;

    public DirectionData(int direction, FactoryMaterials factoryType) {
        updateData(direction, factoryType);
    }

    public void updateData(int direction, FactoryMaterials factoryType) {
        this.direction = direction;
        this.factoryType = factoryType;
    }

    public int getDirection() { return direction; }
    public FactoryMaterials getFactoryType() { return factoryType; }

}
