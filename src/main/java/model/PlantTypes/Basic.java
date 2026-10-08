package model.PlantTypes;

import model.Plant;

public class Basic extends Plant {
    public Basic(String name, String color, double cost) {
        super(name, color, cost);
    }

    @Override
    public double getUniqueValue() {
        return 0.0;
    }

    @Override
    public String getInfo() {
        return super.getInfo()+"Basic plant";
    }
}
