package model.repositories;

import model.User;

import java.util.List;

public interface UserRepository {
    void addUser(User User);
    User getUser(Long id);
    User getUserByLogin(String login);
    List<User> getUsersByLoginPattern(String loginPattern);
    void updateUser(Long id, User user);
    List<User> getUsers();
    List<User> findAllActive();
    List<User> findAllUnactive();
}
