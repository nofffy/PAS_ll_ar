package model.UserTypes;

import model.User;

public class Admin extends User {
    public Admin(String login, boolean active) {
        super(login, active);
    }

    @Override
    public UserType getUserType() {
        return UserType.ADMIN;
    }
}
