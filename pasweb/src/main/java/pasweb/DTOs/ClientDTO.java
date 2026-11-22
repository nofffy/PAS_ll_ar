package pasweb.DTOs;

import jakarta.validation.constraints.*;

public class ClientDTO {
    private String id;
    @NotBlank
    @Size(min = 3, max = 20)
    private String login;
    @NotBlank
    @Size(min = 5, max = 20)
    private String password;
    @NotBlank
    @Email
    private String email;
    @NotNull
    @Min(18)
    @Max(130)
    private Integer age;
    private boolean active;
    private String type;

    public ClientDTO() {}

    public ClientDTO(String login, String password, String email, int age, boolean active) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.age = age;
        this.active = active;
    }

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
