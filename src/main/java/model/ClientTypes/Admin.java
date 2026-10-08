package model.ClientTypes;

import model.User;

public class Admin extends User {
    public Admin(Long id, String login, boolean active) {
        super(id, login, active);
    }
}
