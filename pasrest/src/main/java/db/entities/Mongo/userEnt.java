package db.entities.Mongo;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

public class userEnt {
    @BsonId
    private ObjectId id;
    private String login;
    private boolean active;

    private String userType;
    private Integer maxRents;

    public userEnt() {}

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getMaxRents() {
        return maxRents;
    }

    public void setMaxRents(Integer maxRents) {
        this.maxRents = maxRents;
    }
}
