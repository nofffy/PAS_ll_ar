package pasrest.api.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pasrest.model.UserTypes.UserType;

public class CreateUpdateUserDTO {
    @NotBlank(message = "Login is required")
    private String login;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Email is required")
    private String email;

    @NotNull(message = "Age is required")
    private int age;

    @NotNull(message = "User type is required")
    private UserType type;
    private Integer maxRents;

    private Boolean active;

    public CreateUpdateUserDTO() {}

    public String getLogin() { return login;}

    public void setLogin(String login) { this.login = login; }

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public UserType getType() { return type;}

    public void setType(UserType type) { this.type = type;}

    public Integer getMaxRents() { return maxRents;}

    public void setMaxRents(Integer maxRents) { this.maxRents = maxRents; }

    public Boolean getActive() { return active; }

    public void setActive(Boolean active) { this.active = active; }
}
