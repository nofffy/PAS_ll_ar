package model;

public abstract class Plant {
    private Long id = null; //UUID??
    private String name;
    private PlantType plantType;
    private boolean available = true;

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

    public PlantType getPlantType() {
        return plantType;
    }

    public void setPlantType(PlantType plantType) {
        this.plantType = plantType;
    }

    public String getInfo() {
        return getPlantType().getInfo();
    }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
