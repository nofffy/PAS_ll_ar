package model.PlantTypes;

import model.Plant;

public class Succulent extends Plant {
    private boolean spiky;

    public Succulent(String name, String color, double cost, boolean spiky) {
        super(name, color, cost);
        this.spiky = spiky;
    }

    public void setSpiky(boolean spiky) { this.spiky = spiky; }

    public boolean isSpiky() {
        return spiky;
    }

    @Override
    public double getUniqueValue() {
        return spiky ? 10.0 : 4.0;
    }

    @Override
    public String getInfo() {
        return super.getInfo()+"Succulent plant, is " + (spiky ? "":"not") + " spiky";
    }
}
