package model.ClientTypes;

import model.User;

public class Client extends User {
    public Client(int id, String login, boolean active) {
        super(id, login, active);
    }
}
