package api.DTOs;

public class UserResponseDTO {
    private String id;
    private String login;
    private String type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
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
