package managers;

import Exeptions.ResourceNotFoundException;
import DTOs.UserDTO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.User;
import model.UserTypes.Admin;
import model.UserTypes.Client;
import model.UserTypes.Gardener;
import model.repositories.UserRepository;

import java.util.List;

@RequestScoped
public class UserManager {

    @Inject
    private UserRepository userRepository;

    public void createUser(UserDTO user) {
        User newUser;

        boolean isActive = true;
        if (user.getActive() != null) {
            isActive = user.getActive();
        }

        String type = user.getType();
        if (type.equals("ADMIN")) {
            newUser = new Admin(user.getLogin(), isActive);
        } else if (type.equals("GARDENER")) {
            newUser = new Gardener(user.getLogin(), isActive);
        } else if (type.equals("CLIENT")) {
            newUser = new Client(user.getLogin(), isActive);
        } else {
            throw new IllegalArgumentException("Unknown user type");
        }

        userRepository.addUser(newUser);
    }

    public User getUser(Long id){
        User user = userRepository.getUser(id);
        if (user == null) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        return user;
    }

    public User getUserByLogin(String login) {
        User user = userRepository.getUserByLogin(login);
        if (user == null) {
            throw new ResourceNotFoundException("User with login: " + login + " not found");
        }
        return user;
    }

    public List<User> getUsersByLogin(String loginPattern){
        return userRepository.getUsersByLoginPattern(loginPattern);
    }

    public List<User> getAllUsers(){
        return userRepository.getUsers();
    }

    public void updateUser(Long id, UserDTO user) {
        User existingUser = userRepository.getUser(id);
        if (existingUser == null) {
            throw new ResourceNotFoundException("User with id " + id + " does not exist");
        }

        User updatedUser;

        boolean isActive = true;
        if (user.getActive() != null) {
            isActive = user.getActive();
        }

        String type = user.getType();
        if (type.equals("ADMIN")) {
            updatedUser = new Admin(user.getLogin(), isActive);
        } else if (type.equals("GARDENER")) {
            updatedUser = new Gardener(user.getLogin(), isActive);
        } else if (type.equals("CLIENT")) {
            updatedUser = new Client(user.getLogin(), isActive);
        } else {
            throw new IllegalArgumentException("Unknown user type");
        }

        userRepository.updateUser(id, updatedUser);
    }

    public void activateUser(Long id){
        User user = userRepository.getUser(id);
        if (user == null) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        user.setActive(true);
    }

    public void deactivateUser(Long id){
        User user = userRepository.getUser(id);
        if (user == null) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        user.setActive(false);
    }

    public List<User> findAllActiveUsers(){ return userRepository.findAllActive(); }

    public List<User> findAllUnactiveUsers(){ return userRepository.findAllUnactive(); }
}
