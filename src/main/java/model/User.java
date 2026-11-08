package model;

public abstract class User {
    private String id;
    private String login;
    private boolean active;

    public User(String login, boolean active) {
        this.id = null;
        this.login = login;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


}
