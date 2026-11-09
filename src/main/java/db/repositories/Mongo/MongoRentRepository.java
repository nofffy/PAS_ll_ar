package db.repositories.Mongo;

import Exeptions.ResourceConflictException;
import Exeptions.ResourceNotFoundException;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Updates;
import db.entities.Mongo.rentEnt;
import db.mappers.Mongo.MongoRentMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.Rent;
import model.repositories.RentRepository;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Typed(MongoRentRepository.class)
public class MongoRentRepository implements RentRepository {
    private final MongoCollection<rentEnt> collection;

    public MongoRentRepository() {
        this.collection = null;
    }

    @Inject
    public MongoRentRepository(MongoDatabase database) {
        this.collection = database.getCollection("rents", rentEnt.class);
        collection.createIndex(Indexes.ascending("plant._id"),
                new IndexOptions()
                        .unique(true)
                        .partialFilterExpression(Filters.exists("rentEnd", false))
        );
    }

    @Override
    public Rent getRent(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        if (!ObjectId.isValid(id)) {
            return null;
        }
        rentEnt rent = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        return MongoRentMapper.EntToObj(rent);
    }

    @Override
    public void addRent(Rent rent) {
        if (rent == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        String plantId = rent.getPlant().getId();
        rentEnt ent = MongoRentMapper.ObjToEnt(rent);
        try {
            collection.insertOne(ent);
            rent.setId(ent.getId().toHexString());
        } catch (MongoWriteException e) {
            if (e.getError().getCode() == 11000) {
                throw new ResourceConflictException("Plant with id " + plantId + " is rented");
            }
            throw e;
        }
    }

    @Override
    public void finishRent(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        if (!ObjectId.isValid(id)) {
            throw new ResourceNotFoundException("Rent with id " + id + " not found");
        }
        Bson filter = Filters.and(
                Filters.eq("_id", new ObjectId(id)),
                Filters.exists("rentEnd", false)
        );
        Bson update = Updates.set("rentEnd", LocalDateTime.now());
        rentEnt updated = collection.findOneAndUpdate(filter, update);
        if (updated == null) {
            rentEnt existing = collection.find(Filters.eq("_id", new ObjectId(id))).first();
            if (existing == null) throw new ResourceNotFoundException("Rent with id " + id + " not found");
            throw new ResourceConflictException("Rent is already finished");
        }
    }

    @Override
    public void removeRent(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        if (!ObjectId.isValid(id)) {
            throw new ResourceNotFoundException("Rent with id " + id + " not found");
        }
        Bson filter = Filters.and(
                Filters.eq("_id", new ObjectId(id)),
                Filters.exists("rentEnd", false)
        );
        rentEnt deleted = collection.findOneAndDelete(filter);
        if (deleted == null) {
            rentEnt existing = collection.find(Filters.eq("_id", new ObjectId(id))).first();
            if (existing == null) throw new ResourceNotFoundException("Rent doesn't exist");
            throw new ResourceConflictException("Can't remove finished rents");
        }
    }

    @Override
    public List<Rent> getPastRentsByClientId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        if (!ObjectId.isValid(id)) {
            return List.of();
        }
        Bson filter = Filters.and(
                Filters.eq("client._id", new ObjectId(id)),
                Filters.exists("rentEnd", true)
        );

        return collection.find(filter).into(new ArrayList<>()).stream()
                .map(MongoRentMapper::EntToObj).toList();
    }

    @Override
    public List<Rent> getCurrentRentsByClientId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        if (!ObjectId.isValid(id)) {
            return List.of();
        }
        Bson filter = Filters.and(
                Filters.eq("client._id", new ObjectId(id)),
                Filters.exists("rentEnd", false)
        );

        return collection.find(filter).into(new ArrayList<>()).stream()
                .map(MongoRentMapper::EntToObj).toList();
    }

    @Override
    public List<Rent> getPastRentsByPlantId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        if (!ObjectId.isValid(id)) {
            return List.of();
        }
        Bson filter = Filters.and(
                Filters.eq("plant._id", new ObjectId(id)),
                Filters.exists("rentEnd", true)
        );

        return collection.find(filter).into(new ArrayList<>()).stream()
                .map(MongoRentMapper::EntToObj).toList();
    }

    @Override
    public List<Rent> getCurrentRentsByPlantId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        if (!ObjectId.isValid(id)) {
            return List.of();
        }
        Bson filter = Filters.and(
                Filters.eq("plant._id", new ObjectId(id)),
                Filters.exists("rentEnd", false)
        );

        return collection.find(filter).into(new ArrayList<>()).stream()
                .map(MongoRentMapper::EntToObj).toList();
    }

    @Override
    public List<Rent> getRents() {
        return collection.find().into(new ArrayList<>()).stream()
                .map(MongoRentMapper::EntToObj).toList();
    }
}
