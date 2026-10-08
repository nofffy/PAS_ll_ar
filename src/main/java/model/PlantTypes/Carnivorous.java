package model.PlantTypes;

import model.PlantType;

public class Carnivorous implements PlantType {
    private String favouriteFood;

    public Carnivorous(String favouriteFood) {
        this.favouriteFood = favouriteFood;
    }

    public String getFavouriteFood() {
        return favouriteFood;
    }

    public void setFavouriteFood(String favouriteFood) {
        this.favouriteFood= favouriteFood;
    }

    @Override
    public double getUniqueValue() {
        if(favouriteFood.toLowerCase().contains("fly")) {
            return 15.0;
        }
        else {
            return 7.0;
        }
    }

    @Override
    public String getInfo() {
        return "Carnivorous plant, favourite food: "+favouriteFood;
    }
}
