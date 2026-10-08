package model;

public class User {
    private int id;
    private String login;
    private boolean active;

    public User(int id, String login, boolean active) {
        this.id = id;
        this.login = login;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
