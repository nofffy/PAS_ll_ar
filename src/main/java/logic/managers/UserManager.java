package logic.managers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import model.User;
import model.repositories.MemoryRepositories.MemoryUserRepository;

import java.util.List;

@ApplicationScoped
public class UserManager {

    @Inject
    private MemoryUserRepository mur; //murr :3

    public void createUser(User userDTO){
        mur.addUser(userDTO);
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
        mur.getUser(id).setActive(true);
    }
    public void deactivateUser(Long id){
        mur.getUser(id).setActive(false);
    }
}
