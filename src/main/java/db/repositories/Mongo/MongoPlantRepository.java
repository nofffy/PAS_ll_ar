package db.repositories.Mongo;

import Exeptions.ResourceNotFoundException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import db.entities.Mongo.plantEnt;
import db.mappers.Mongo.MongoPlantMapper;
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

    public MongoPlantRepository() {
        this.collection = null;
    }

    @Inject
    public MongoPlantRepository(MongoDatabase database) {
        this.collection = database.getCollection("plants", plantEnt.class);
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
        plantEnt ent = MongoPlantMapper.ObjToEnt(plant);
        UpdateResult result = collection.replaceOne(Filters.eq("_id", new ObjectId(plant.getId())), ent);
        if (result.getMatchedCount() == 0) {
            throw new ResourceNotFoundException("Plant with id " + plant.getId() + " not found");
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
        DeleteResult result = collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
        if (result.getDeletedCount() == 0) {
            throw new ResourceNotFoundException("Plant with id " + id + " not found");
        }
    }

    @Override
    public List<Plant> getPlants() {
        return collection.find().into(new ArrayList<>()).stream()
                .map(MongoPlantMapper::EntToObj).toList();
    }
}
