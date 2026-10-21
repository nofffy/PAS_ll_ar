package managers;

import dtos.UserDTO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.User;
import model.UserTypes.Admin;
import model.UserTypes.Client;
import model.UserTypes.Gardener;
import model.repositories.MemoryRepositories.MemoryUserRepository;

import java.util.List;

@RequestScoped
public class UserManager {

    @Inject
    private MemoryUserRepository mur;

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

        mur.addUser(newUser);
    }

    public User getUser(Long id){
        return mur.getUser(id);
    }

    public User getUserByLogin(String login){
        return mur.getUserByLogin(login);
    }

    public List<User> getUsersByLogin(String loginPattern){
        return mur.getUsersByLoginPattern(loginPattern);
    }

    public List<User> getAllUsers(){
        return mur.getUsers();
    }

    public void activateUser(Long id){
        User user = mur.getUser(id);
        if (user == null) {
            throw new IllegalArgumentException();
        }
        user.setActive(true);
    }

    public void deactivateUser(Long id){
        User user = mur.getUser(id);
        if (user == null) {
            throw new IllegalArgumentException();
        }
        user.setActive(false);
    }

    public List<User> findAllActiveUsers(){ return mur.findAllActive(); }

    public List<User> findAllUnactiveUsers(){ return mur.findAllUnactive(); }
}
