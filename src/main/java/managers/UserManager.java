package managers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import model.User;
import model.repositories.MemoryRepositories.MemoryUserRepository;

import java.util.List;

@ApplicationScoped
public class UserManager {

    @Inject
    private MemoryUserRepository mur;

    public void createUser(User user) { mur.addUser(user); }

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
