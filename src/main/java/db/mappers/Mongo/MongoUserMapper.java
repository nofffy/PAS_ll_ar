package db.mappers.Mongo;

import db.entities.Mongo.userEnt;
import model.User;
import model.UserTypes.Admin;
import model.UserTypes.Client;
import model.UserTypes.Gardener;
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
        if (user instanceof Admin) {
            ent.setUserType("ADMIN");
        } else if (user instanceof Gardener) {
            ent.setUserType("GARDENER");
        } else if (user instanceof Client c) {
            ent.setUserType("CLIENT");
            ent.setMaxRents(c.getMaxRents());
        }

        return ent;
    }

    public static User EntToObj(userEnt userEnt) {
        if (userEnt == null) {
            return null;
        }

        String id = userEnt.getId() != null ? userEnt.getId().toHexString() : null;
        User user;
        String type = userEnt.getUserType();
        if (type.equals("ADMIN")) {
            user = new Admin(userEnt.getLogin(), userEnt.isActive());
        } else if (type.equals("GARDENER")) {
            user = new Gardener(userEnt.getLogin(), userEnt.isActive());
        } else if (type.equals("CLIENT")) {
            user = new Client(userEnt.getLogin(), userEnt.isActive(), userEnt.getMaxRents());
        } else {
            throw new IllegalStateException("Unknown user type: " + type);
        }
        user.setId(id);
        return user;
    }
}
