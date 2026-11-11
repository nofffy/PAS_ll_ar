package db.entities.Mongo;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

public class plantEnt {
    @BsonId
    private ObjectId id;
    private String name;
    private String color;
    private double cost;

    private String plantType;
    private String favouriteFood;
    private Integer leafSize;
    private Boolean spiky;

    public plantEnt() {}

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
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

    public String getPlantType() {
        return plantType;
    }

    public void setPlantType(String plantType) {
        this.plantType = plantType;
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

    public Boolean getSpiky() {
        return spiky;
    }

    public void setSpiky(Boolean spiky) {
        this.spiky = spiky;
    }
}
