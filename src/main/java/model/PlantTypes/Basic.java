package model.PlantTypes;

import model.Plant;

public class Basic extends Plant {
    public Basic(String name, String color, double cost, boolean available) {
        super(name, color, cost, available);
    }

    @Override
    public double getUniqueValue() {
        return 0.0;
    }
}
