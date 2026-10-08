package model;

public class Plant {
    private Long id; //UUID??
    private String name;
    private String color;
    private double cost;
    private boolean sold;
    private PlantType plantType;

//    public Plant(Long id, String name, String color, double cost, boolean sold, PlantType plantType) {
//        this.id = id;
//        this.name = name;
//        this.color = color;
//        this.cost = cost;
//        this.sold = sold;
//        this.plantType = plantType;
//    }

    public Plant(String name, String color, double cost, boolean sold, PlantType plantType) {
        this.id = null;
        this.name = name;
        this.color = color;
        this.cost = cost;
        this.sold = sold;
        this.plantType = plantType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    public PlantType getPlantType() {
        return plantType;
    }

    public void setPlantType(PlantType plantType) {
        this.plantType = plantType;
    }

    public String getInfo() {
        return getPlantType().getInfo();
    }
}
