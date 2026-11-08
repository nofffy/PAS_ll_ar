package model.UserTypes;

import model.User;

public class Client extends User {
    public final int MAX_RENTS = 5;
    private int maxRents;

    public Client(String login, boolean active, int maxRents) {
        super(login, active);
        this.maxRents = maxRents;
    }

    public Client(String login, boolean active) {
        super(login, active);
        this.maxRents = MAX_RENTS;
    }

    public int getMaxRents() {
        return maxRents;
    }

    public void setMaxRents(int maxRents) {
        this.maxRents = maxRents;
    }
}
