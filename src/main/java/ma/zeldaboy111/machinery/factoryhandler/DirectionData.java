package ma.zeldaboy111.machinery.factoryhandler;

import ma.zeldaboy111.machinery.FactoryMaterials;

public class DirectionData {

    private int direction;
    private FactoryMaterials factoryType;

    public DirectionData(int direction, FactoryMaterials factoryType) {
        updateData(direction, factoryType);
    }

    public void updateData(DirectionData data) { updateData(data.getDirection(), data.getFactoryType()); }
    public void updateData(int direction, FactoryMaterials factoryType) {
        this.direction = direction;
        this.factoryType = factoryType;
    }

    public int getDirection() { return direction; }
    public FactoryMaterials getFactoryType() { return factoryType; }

}
