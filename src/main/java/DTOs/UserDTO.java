package DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserDTO {
    @NotBlank(message = "Login is required")
    private String login;

    @NotBlank(message = "User type is required")
    @Pattern(regexp = "^(ADMIN|GARDENER|CLIENT)$", message = "User type has to be one of: ADMIN, GARDENER, CLIENT")
    private String type;

    private Boolean active;

    public UserDTO() {}

    public String getLogin() { return login;}

    public void setLogin(String login) { this.login = login; }

    public String getType() { return type;}

    public void setType(String type) { this.type = type;}

    public Boolean getActive() { return active; }

    public void setActive(Boolean active) { this.active = active; }
}
