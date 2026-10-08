package model.repositories;

import model.User;

import java.util.List;

public interface UserRepository {
    User getUser(int id);
    boolean addUser(User User);
    boolean updateUser(User user);
    boolean removeUser(int id);

    boolean saveUsers(List<User> users);
    List<User> getUsers();
    int sizeUsers();
}
