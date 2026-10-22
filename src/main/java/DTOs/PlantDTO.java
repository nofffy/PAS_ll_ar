package DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class PlantDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Color is required")
    private String color;

    @NotNull(message = "Cost is required")
    @Positive(message = "Cost must be greater than 0")
    private Double cost;

    private Boolean available;

    @NotBlank(message = "Plant type is required")
    @Pattern(regexp = "^(BASIC|CARNIVOROUS|MONSTERA|SUCCULENT)$", message = "Plant type has to be one of: BASIC, CARNIVOROUS, MONSTERA, SUCCULENT")
    private String type;

    private String favouriteFood;
    private Integer leafSize;
    private Boolean spiky;

    public PlantDTO() {}

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

    public Boolean isAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    public void setSpiky(Boolean spiky) { this.spiky = spiky; }
}
