package model.PlantTypes;

import model.PlantType;

public class Succulent implements PlantType {
    private final boolean spiky;

    public Succulent(boolean spiky) {
        this.spiky = spiky;
    }

    public boolean isSpiky() {
        return spiky;
    }

    @Override
    public double getUniqueValue() {
        return spiky ? 10.0 : 4.0;
    }

    @Override
    public String getInfo() {
        return "Succulent plant, is " + (spiky ? "":"not") + " spiky";
    }
}
