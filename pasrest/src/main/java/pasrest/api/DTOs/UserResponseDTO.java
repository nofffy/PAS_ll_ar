package pasrest.api.DTOs;

import pasrest.model.UserTypes.UserType;

public class UserResponseDTO {
    private String id;
    private String login;
    private UserType type;
    private Boolean active;

    private Integer maxRents;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getMaxRents() {
        return maxRents;
    }

    public void setMaxRents(Integer maxRents) {
        this.maxRents = maxRents;
    }
}
