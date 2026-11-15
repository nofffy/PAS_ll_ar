package db.mappers.Mongo;

import db.entities.Mongo.userEnt;
import model.PlantTypes.PlantType;
import model.User;
import model.UserTypes.Admin;
import model.UserTypes.Client;
import model.UserTypes.Gardener;
import model.UserTypes.UserType;
import org.bson.types.ObjectId;

public class MongoUserMapper {
    public static userEnt ObjToEnt(User user) {
        if (user == null) {
            return null;
        }

        userEnt ent = new userEnt();
        if (user.getId() != null && ObjectId.isValid(user.getId())) {
            ent.setId(new ObjectId(user.getId()));
        }
        ent.setLogin(user.getLogin());
        ent.setActive(user.isActive());
        UserType type = user.getUserType();
        ent.setUserType(type.name());

        switch (type) {
            case ADMIN -> {}
            case GARDENER -> {}
            case CLIENT -> {ent.setMaxRents(((Client) user).getMaxRents());}
        }
        return ent;
    }

    public static User EntToObj(userEnt userEnt) {
        if (userEnt == null) {
            return null;
        }

        String id = userEnt.getId() != null ? userEnt.getId().toHexString() : null;
        UserType type;
        try {
            type = UserType.valueOf(userEnt.getUserType());
        } catch (Exception e) {
            throw new IllegalStateException("Unknown user type: " + userEnt.getUserType());
        }

        User user = switch (type) {
            case ADMIN -> new Admin(userEnt.getLogin(), userEnt.isActive());
            case GARDENER -> new Gardener(userEnt.getLogin(), userEnt.isActive());
            case CLIENT -> new Client(userEnt.getLogin(), userEnt.isActive(), userEnt.getMaxRents());
        };
        user.setId(id);
        return user;
    }
}
