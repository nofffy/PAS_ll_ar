package model.PlantTypes;

import model.PlantType;

public class Monstera implements PlantType {
    private final int leafSize;

    public Monstera(int leafSize) {
        if(leafSize < 0) throw new IllegalArgumentException("leafSize cannot be negative");
        this.leafSize = leafSize;
    }

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
        return "Monstera plant, leaf size: "+leafSize;
    }

}
