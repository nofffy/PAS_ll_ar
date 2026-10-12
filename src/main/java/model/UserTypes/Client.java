package model.UserTypes;

import model.User;

public class Client extends User {
    private double moneySpent; //TODO: niepotrzebne albo do implementacji w RentRepo

    public Client(String login, boolean active) {
        super(login, active);
        this.moneySpent = 0;
    }

    public double getMoneySpent() {
        return moneySpent;
    }

    public void setMoneySpent(double moneySpent) {
        this.moneySpent = moneySpent;
    }
}
