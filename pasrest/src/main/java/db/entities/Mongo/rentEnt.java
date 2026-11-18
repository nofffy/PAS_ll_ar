package db.entities.Mongo;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public class rentEnt {
    @BsonId
    private ObjectId id;
    private userEnt client;
    private plantEnt plant;
    private LocalDateTime rentStart;
    private LocalDateTime rentEnd;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public userEnt getClient() {
        return client;
    }

    public void setClient(userEnt client) {
        this.client = client;
    }

    public plantEnt getPlant() {
        return plant;
    }

    public void setPlant(plantEnt plant) {
        this.plant = plant;
    }

    public LocalDateTime getRentStart() {
        return rentStart;
    }

    public void setRentStart(LocalDateTime rentStart) {
        this.rentStart = rentStart;
    }

    public void endRent() { this.rentEnd = LocalDateTime.now(); }

    public LocalDateTime getRentEnd() {
        return rentEnd;
    }

    public void setRentEnd(LocalDateTime rentEnd) {
        this.rentEnd = rentEnd;
    }
}
