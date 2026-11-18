package pasrest.api.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import pasrest.model.PlantTypes.PlantType;

public class CreateUpdatePlantDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Color is required")
    private String color;

    @NotNull(message = "Cost is required")
    @Positive(message = "Cost must be greater than 0")
    private Double cost;

    @NotNull(message = "Plant type is required")
    private PlantType type;

    private String favouriteFood;
    @Positive(message = "Leaf size must be greater than 0")
    private Integer leafSize;
    private Boolean spiky;

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

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public PlantType getType() {
        return type;
    }

    public void setType(PlantType type) {
        this.type = type;
    }

    public String getFavouriteFood() {
        return favouriteFood;
    }

    public void setFavouriteFood(String favouriteFood) {
        this.favouriteFood = favouriteFood;
    }

    public Integer getLeafSize() {
        return leafSize;
    }

    public void setLeafSize(Integer leafSize) {
        this.leafSize = leafSize;
    }

    public Boolean isSpiky() {
        return spiky;
    }

    public void setSpiky(Boolean spiky) {
        this.spiky = spiky;
    }
}
