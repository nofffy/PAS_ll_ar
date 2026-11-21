package pasrest.model.UserTypes;

import pasrest.model.User;

public class Admin extends User {
    public Admin(String login, boolean active) {
        super(login, active);
    }

    public Admin(String login, String password, String email, int age, boolean active) {
        super(login, password, email, age, active);
    }

    @Override
    public UserType getUserType() {
        return UserType.ADMIN;
    }
}
