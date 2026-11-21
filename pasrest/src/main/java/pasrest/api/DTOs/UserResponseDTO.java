package pasrest.api.DTOs;

import pasrest.model.UserTypes.UserType;

public class UserResponseDTO {
    private String id;
    private String login;
    private String password;
    private String email;
    private Integer age;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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
