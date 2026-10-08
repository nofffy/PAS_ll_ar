package model.repositories;

import model.User;

import java.util.List;

public interface UserRepository {
    boolean addUser(User User);
    User getUser(Long id);
    User getUserByLogin(String login);
    List<User> getUsersByLoginPattern(String loginPattern);
    //boolean saveUsers(List<User> users);
    //boolean updateUser(User user);

    boolean activateUser(Long id);
    boolean deactivateUser(Long id);
    List<User> getUsers();
    List<User> findAllActive();
    List<User> findAllUnactive();
    //int sizeUsers();
}
