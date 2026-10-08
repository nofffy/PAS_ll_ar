package model.PlantTypes;

import model.Plant;

public class Monstera extends Plant {
    private int leafSize;

    public Monstera(String name, String color, double cost, int leafSize) {
        super(name, color, cost);
        this.leafSize = leafSize;
    }

    public void setLeafSize(int leafSize) { this.leafSize = leafSize; }

    public int getLeafSize() {
        return leafSize;
    }

    @Override
    public double getUniqueValue() {
        if(leafSize > 25) {
            return 30.0;
        }
        else if(leafSize > 20) {
            return 22.0;
        }
        else if(leafSize > 15) {
            return 15.0;
        }
        else if(leafSize > 10) {
            return 10.0;
        }
        else {
            return leafSize*0.8;
        }
    }

    @Override
    public String getInfo() {
        return super.getInfo()+"Monstera plant, leaf size: "+leafSize;
    }

}
