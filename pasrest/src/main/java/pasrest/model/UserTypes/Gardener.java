package pasrest.model.UserTypes;

import pasrest.model.User;

public class Gardener extends User {
    public Gardener(String login, boolean active) {
        super(login, active);
    }

    public Gardener(String login, String password, String email, int age, boolean active) {
        super(login, password, email, age, active);
    }

    @Override
    public UserType getUserType() {
        return UserType.GARDENER;
    }
}
