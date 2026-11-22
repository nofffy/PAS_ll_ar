package pasweb.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PlantDTO {
    private String id;
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Color is required")
    private String color;

    @NotNull(message = "Cost is required")
    @Positive(message = "Cost must be greater than 0")
    private Double cost;

    @NotNull(message = "Plant type is required")
    private String type;

    public PlantDTO() {}

    public PlantDTO(String name, String color, Double cost, String type) {
        this.name = name;
        this.color = color;
        this.cost = cost;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
