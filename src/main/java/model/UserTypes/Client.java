package model.UserTypes;

import model.User;

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

    public int getMaxRents() {
        return maxRents;
    }

    public void setMaxRents(int maxRents) {
        this.maxRents = maxRents;
    }
}
