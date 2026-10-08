package model.ClientTypes;

import model.User;

public class Client extends User {
    public Client(Long id, String login, boolean active) {
        super(id, login, active);
    }
}
