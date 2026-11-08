package db.repositories.Mongo;

import Exeptions.ResourceConflictException;
import Exeptions.ResourceNotFoundException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
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
        if (plantId != null && ObjectId.isValid(plantId)) {
            Bson activeFilter = Filters.and(
                    Filters.eq("plant._id", new ObjectId(plantId)),
                    Filters.exists("rentEnd", false)
            );
            if (collection.find(activeFilter).first() != null) {
                throw new ResourceConflictException("Plant with id " + plantId + " is rented");
            }
        }
        rentEnt ent = MongoRentMapper.ObjToEnt(rent);
        collection.insertOne(ent);
        rent.setId(ent.getId().toHexString());
    }

    @Override
    public void finishRent(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Can't be null");
        }

        rentEnt ent = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        if (ent == null) {
            throw new ResourceNotFoundException("Rent with id " + id + " not found");
        }
        if (ent.getRentEnd() != null) {
            throw new ResourceConflictException("Rent is already finished");
        }
        ent.endRent();
        collection.replaceOne(Filters.eq("_id", new ObjectId(id)), ent);
    }

    @Override
    public void removeRent(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        rentEnt ent = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        if (ent == null) {
            throw new ResourceNotFoundException("Rent doesn't exist");
        }
        if (ent.getRentEnd() != null) {
            throw new ResourceConflictException("Can't remove finished rents");
        }
        collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
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
