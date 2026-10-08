package model.PlantTypes;

import model.Plant;

public class Carnivorous extends Plant {
    private String favouriteFood;

    public Carnivorous(String name, String color, double cost, String favouriteFood) {
        super(name, color, cost);
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
        return super.getInfo()+"Carnivorous plant, favourite food: "+favouriteFood;
    }
}
