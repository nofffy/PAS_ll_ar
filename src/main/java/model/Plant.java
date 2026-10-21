package model;

public abstract class Plant {
    private Long id = null; //UUID??
    private String name;
    private String color;
    private double cost;
    private boolean available;

    public Plant(String name, String color, double cost, boolean available) {
        this.name = name;
        this.color = color;
        this.cost = cost;
        this.available = available;
    }

    public abstract double getUniqueValue();

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

    public boolean isAvailable() { return available; }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Long getId() { return id; }

    public void setId(Long id) {
        this.id = id;
    }
}
