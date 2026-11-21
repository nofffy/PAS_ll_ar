package pasrest.model;

import pasrest.model.UserTypes.UserType;

public abstract class User {
    private String id;
    private String login;
    private String password;
    private String email;
    private int age;
    private boolean active;

    public User(String login, boolean active) {
        this.id = null;
        this.login = login;
        this.active = active;
    }

    public User(String login, String password, String email, int age, boolean active) {
        this.id = null;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public abstract UserType getUserType();
}
