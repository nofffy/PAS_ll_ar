package pasrest.api.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pasrest.model.UserTypes.UserType;

public class CreateUpdateUserDTO {
    @NotBlank(message = "Login is required")
    private String login;

    @NotNull(message = "User type is required")
    private UserType type;
    private Integer maxRents;

    private Boolean active;

    public CreateUpdateUserDTO() {}

    public String getLogin() { return login;}

    public void setLogin(String login) { this.login = login; }

    public UserType getType() { return type;}

    public void setType(UserType type) { this.type = type;}

    public Integer getMaxRents() { return maxRents;}

    public void setMaxRents(Integer maxRents) { this.maxRents = maxRents; }

    public Boolean getActive() { return active; }

    public void setActive(Boolean active) { this.active = active; }
}
