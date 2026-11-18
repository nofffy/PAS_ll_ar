package db.mappers.Mongo;

import db.entities.Mongo.rentEnt;
import model.Plant;
import model.Rent;
import model.User;
import org.bson.types.ObjectId;

public class MongoRentMapper {
    public static rentEnt ObjToEnt(Rent rent) {
        if (rent == null) return null;

        rentEnt ent = new rentEnt();
        if (rent.getId() != null && ObjectId.isValid(rent.getId())) {
            ent.setId(new ObjectId(rent.getId()));
        }

        ent.setClient(MongoUserMapper.ObjToEnt(rent.getClient()));
        ent.setPlant(MongoPlantMapper.ObjToEnt(rent.getPlant()));
        ent.setRentStart(rent.getRentStart());
        if (rent.getRentEnd() != null) {
            ent.setRentEnd(rent.getRentEnd());
        }
        return ent;
    }

    public static Rent EntToObj(rentEnt ent) {
        if (ent == null) return null;

        String domainId = ent.getId().toHexString();
        User client = MongoUserMapper.EntToObj(ent.getClient());
        Plant plant = MongoPlantMapper.EntToObj(ent.getPlant());

        Rent rent = new Rent(client, plant, ent.getRentStart());

        if (ent.getRentEnd() != null) {
            rent.setRentEnd(ent.getRentEnd());
        }

        rent.setId(domainId);
        return rent;
    }
}
