package model;

public abstract class Plant {
    private Long id; //UUID??
    private String name;
    private String color;
    private double cost;
    private boolean available;

//    public Plant(Long id, String name, String color, double cost, boolean sold, PlantType plantType) {
//        this.id = id;
//        this.name = name;
//        this.color = color;
//        this.cost = cost;
//        this.sold = sold;
//        this.plantType = plantType;
//    }

    public Plant(String name, String color, double cost) {
        this.id = null;
        this.name = name;
        this.color = color;
        this.cost = cost;
        this.available = true;
    }

    public abstract double getUniqueValue();

    public String getInfo() {
        return "Name: " + name + ", Color: " + color + ", Cost: " + cost +  ", Available: " + available + "; ";
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

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
