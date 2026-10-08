package model.PlantTypes;

import model.PlantType;

public class Basic implements PlantType {
    public Basic() {
    }

    @Override
    public double getUniqueValue() {
        return 0.0;
    }

    @Override
    public String getInfo() {
        return "Normal plant";
    }
}
