package pasrest.model.UserTypes;

import pasrest.model.User;

public class Client extends User {
    private int maxRents;

    public Client(String login, boolean active, int maxRents) {
        super(login, active);
        this.maxRents = maxRents;
    }

    public Client(String login, boolean active) {
        super(login, active);
        this.maxRents = 5;
    }

    public Client(String login, String password, String email, int age, boolean active, int maxRents) {
        super(login, password, email, age, active);
        this.maxRents = maxRents;
    }

    public Client(String login, String password, String email, int age, boolean active) {
        super(login, password, email, age, active);
        this.maxRents = 5;
    }

    public int getMaxRents() {
        return maxRents;
    }

    public void setMaxRents(int maxRents) {
        this.maxRents = maxRents;
    }

    @Override
    public UserType getUserType() {
        return UserType.CLIENT;
    }
}
