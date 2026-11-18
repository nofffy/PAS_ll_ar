package pasrest.db.mappers.Mongo;

import pasrest.db.entities.Mongo.userEnt;
import pasrest.model.User;
import pasrest.model.UserTypes.Admin;
import pasrest.model.UserTypes.Client;
import pasrest.model.UserTypes.Gardener;
import pasrest.model.UserTypes.UserType;
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
