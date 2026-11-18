package db.mappers.Mongo;

import db.entities.Mongo.plantEnt;
import model.Plant;
import model.PlantTypes.*;
import org.bson.types.ObjectId;

public class MongoPlantMapper {
    public static plantEnt ObjToEnt(Plant plant) {
        if (plant == null) {
            return null;
        }

        plantEnt ent = new plantEnt();
        if (plant.getId() != null && ObjectId.isValid(plant.getId())) {
            ent.setId(new ObjectId(plant.getId()));
        }
        ent.setName(plant.getName());
        ent.setColor(plant.getColor());
        ent.setCost(plant.getCost());
        PlantType type = plant.getType();
        ent.setPlantType(type.name());

        switch (type) {
            case BASIC -> {}
            case CARNIVOROUS -> ent.setFavouriteFood(((Carnivorous) plant).getFavouriteFood());
            case MONSTERA -> ent.setLeafSize(((Monstera) plant).getLeafSize());
            case SUCCULENT -> ent.setSpiky(((Succulent) plant).isSpiky());
        }
        return ent;
    }

    public static Plant EntToObj(plantEnt ent) {
        if (ent == null) return null;

        String id = ent.getId() != null ? ent.getId().toHexString() : null;
        PlantType type;
        try {
            type = PlantType.valueOf(ent.getPlantType());
        } catch (Exception e) {
            throw new IllegalStateException("Unknown plant type: " + ent.getPlantType());
        }

        Plant plant = switch (type) {
            case BASIC -> new Basic(ent.getName(), ent.getColor(), ent.getCost());
            case CARNIVOROUS -> new Carnivorous(ent.getName(), ent.getColor(), ent.getCost(), ent.getFavouriteFood());
            case MONSTERA -> new Monstera(ent.getName(), ent.getColor(), ent.getCost(), ent.getLeafSize());
            case SUCCULENT -> new Succulent(ent.getName(), ent.getColor(), ent.getCost(), ent.getSpiky());
        };
        plant.setId(id);
        return plant;
    }
}
