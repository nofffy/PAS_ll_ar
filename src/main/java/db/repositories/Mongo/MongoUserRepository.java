package db.repositories.Mongo;

import Exeptions.UserLoginAlreadyUsedException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import db.entities.Mongo.userEnt;
import db.mappers.Mongo.MongoUserMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.User;
import model.repositories.UserRepository;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Typed(MongoUserRepository.class)
public class MongoUserRepository implements UserRepository {
    private final MongoCollection<userEnt> collection;

    public MongoUserRepository() {
        this.collection = null;
    }

    @Inject
    public MongoUserRepository(MongoDatabase database) {
        this.collection = database.getCollection("users", userEnt.class);
    }

    @Override
    public void addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        if (collection.find(Filters.eq("login", user.getLogin())).first() != null) {
            throw new UserLoginAlreadyUsedException("User with login " + user.getLogin() + " already exists");
        }
        userEnt ent = MongoUserMapper.ObjToEnt(user);
        collection.insertOne(ent);
        user.setId(ent.getId().toHexString());
    }

    @Override
    public User getUser(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        if (!ObjectId.isValid(id)) {
            return null;
        }
        userEnt ent = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        return MongoUserMapper.EntToObj(ent);
    }

    @Override
    public User getUserByLogin(String login) {
        if (login == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        userEnt ent = collection.find(Filters.eq("login", login)).first();
        return MongoUserMapper.EntToObj(ent);
    }


    @Override
    public List<User> getUsersByLoginPattern(String loginPattern) {
        if (loginPattern == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        Bson filter = Filters.regex("login", loginPattern, "i");
        return collection.find(filter).into(new ArrayList<>()).stream()
                .map(MongoUserMapper::EntToObj).toList();
    }

    @Override
    public List<User> getUsers() {
        return collection.find().into(new ArrayList<>()).stream()
                .map(MongoUserMapper::EntToObj).toList();
    }

    @Override
    public void updateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        if (!ObjectId.isValid(user.getId())) {
            return;
        }
        userEnt ent = MongoUserMapper.ObjToEnt(user);
        collection.replaceOne(Filters.eq("_id", new ObjectId(user.getId())), ent);
    }

    @Override
    public List<User> findAllActive() {
        Bson filter = Filters.and(Filters.eq("active", true));
        return collection.find(filter).into(new ArrayList<>()).stream()
                .map(MongoUserMapper::EntToObj).toList();
    }

    @Override
    public List<User> findAllInactive() {
        Bson filter = Filters.and(Filters.eq("active", false));
        return collection.find(filter).into(new ArrayList<>()).stream()
                .map(MongoUserMapper::EntToObj).toList();
    }
}
