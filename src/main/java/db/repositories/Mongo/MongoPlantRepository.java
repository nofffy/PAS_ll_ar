package db.repositories.Mongo;

import Exeptions.ResourceConflictException;
import Exeptions.ResourceNotFoundException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import db.entities.Mongo.plantEnt;
import db.entities.Mongo.rentEnt;
import db.mappers.Mongo.MongoPlantMapper;
import db.mappers.Mongo.MongoRentMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.Plant;
import model.repositories.PlantRepository;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Typed(MongoPlantRepository.class)
public class MongoPlantRepository implements PlantRepository {
    private final MongoCollection<plantEnt> collection;
    private final MongoCollection<rentEnt> rentCollection;

    public MongoPlantRepository() {
        this.collection = null;
        this.rentCollection = null;
    }

    @Inject
    public MongoPlantRepository(MongoDatabase database) {
        this.collection = database.getCollection("plants", plantEnt.class);
        this.rentCollection = database.getCollection("rents", rentEnt.class);
    }

    @Override
    public void addPlant(Plant plant) {
        if (plant == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        plantEnt ent = MongoPlantMapper.ObjToEnt(plant);
        collection.insertOne(ent);
        plant.setId(ent.getId().toHexString());
    }

    @Override
    public Plant getPlant(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        if (!ObjectId.isValid(id)) {
            return null;
        }
        plantEnt ent = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        if (ent == null) {
            throw new ResourceNotFoundException("Plant with id " + id + " not found");
        }
        return MongoPlantMapper.EntToObj(ent);
    }

    @Override
    public void updatePlant(Plant plant) {
        if (plant == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        if (!ObjectId.isValid(plant.getId())) {
            throw new ResourceNotFoundException("Plant with id " + plant.getId() + " not found");
        }

        ObjectId plantId = new ObjectId(plant.getId());
        plantEnt newEnt = MongoPlantMapper.ObjToEnt(plant);

        Bson update = Updates.combine(
                Updates.set("name", newEnt.getName()),
                Updates.set("color", newEnt.getColor()),
                Updates.set("cost", newEnt.getCost()),
                Updates.set("plantType", newEnt.getPlantType()),
                Updates.set("favouriteFood", newEnt.getFavouriteFood()),
                Updates.set("leafSize", newEnt.getLeafSize()),
                Updates.set("spiky", newEnt.getSpiky())
        );

        UpdateResult result = collection.updateOne(Filters.eq("_id", plantId), update);

        if (result.getMatchedCount() == 0) {
            throw new ResourceNotFoundException("Plant with id " + plantId + " not found");
        }

    }

    @Override
    public void removePlant(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        if (!ObjectId.isValid(id)) {
            throw new ResourceNotFoundException("Plant with id " + id + " not found");
        }
        DeleteResult result = collection.deleteOne(
                Filters.and(
                        Filters.eq("_id", new ObjectId(id)),
                        Filters.eq("hasRentHistory",  false)
                )
        );
        if (result.getDeletedCount() == 0) {
            boolean exists = collection.find(Filters.eq("_id", new ObjectId(id))).first() != null;
            if (!exists) {
                System.out.println("No plantEnt with id " + id + " found");
                throw new ResourceNotFoundException("Plant with id " + id + " not found");
            } else {
                System.out.println("No plantEnt with id " + id + " has rentHistroy");
                throw new ResourceConflictException("Plant with id " + id + " has rent history");
            }
        }
    }

    @Override
    public List<Plant> getPlants() {
        return collection.find().into(new ArrayList<>()).stream()
                .map(MongoPlantMapper::EntToObj).toList();
    }
}
