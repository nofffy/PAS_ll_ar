package model.ClientTypes;

import model.User;

public class Client extends User {
    private double moneySpent;

    public Client(Long id, String login, boolean active) {
        super(id, login, active);
        this.moneySpent = 0;
    }

    public double getMoneySpent() {
        return moneySpent;
    }

    public void setMoneySpent(double moneySpent) {
        this.moneySpent = moneySpent;
    }
}
