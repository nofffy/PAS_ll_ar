package db.mappers.Mongo;

import db.entities.Mongo.plantEnt;
import model.Plant;
import model.PlantTypes.Basic;
import model.PlantTypes.Carnivorous;
import model.PlantTypes.Monstera;
import model.PlantTypes.Succulent;
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
        ent.setAvailable(plant.isAvailable());

        if (plant instanceof Basic) {
            ent.setPlantType("BASIC");
        } else if (plant instanceof Carnivorous c) {
            ent.setPlantType("CARNIVOROUS");
            ent.setFavouriteFood(c.getFavouriteFood());
        } else if (plant instanceof Monstera m) {
            ent.setPlantType("MONSTERA");
            ent.setLeafSize(m.getLeafSize());
        } else if (plant instanceof Succulent s) {
            ent.setPlantType("SUCCULENT");
            ent.setSpiky(s.isSpiky());
        }
        return ent;
    }

    public static Plant EntToObj(plantEnt ent) {
        if (ent == null) return null;

        String id = ent.getId() != null ? ent.getId().toHexString() : null;
        Plant plant;

        String type = ent.getPlantType();
        if (type.equals("BASIC")) {
            plant = new Basic(ent.getName(), ent.getColor(), ent.getCost(), ent.isAvailable());
        } else if (type.equals("CARNIVOROUS")) {
            plant = new Carnivorous(ent.getName(), ent.getColor(), ent.getCost(), ent.isAvailable(), ent.getFavouriteFood());
        } else if (type.equals("MONSTERA")) {
            plant = new Monstera(ent.getName(), ent.getColor(), ent.getCost(), ent.isAvailable(), ent.getLeafSize());
        } else if (type.equals("SUCCULENT")) {
            plant = new Succulent(ent.getName(), ent.getColor(), ent.getCost(), ent.isAvailable(), ent.getSpiky());
        } else {
            throw new IllegalStateException("Unknown plant type: " + type);
        }
        plant.setId(id);
        return plant;
    }
}
