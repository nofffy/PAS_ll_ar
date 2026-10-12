package model.repositories.MemoryRepositories;

import plantExeptions.UserLoginAlreadyUsedException;
import model.User;
import model.repositories.UserRepository;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@ApplicationScoped
public class MemoryUserRepository implements UserRepository {

    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final Map<String, User> usersByLogin = new ConcurrentHashMap<>();

    private final AtomicLong curId = new AtomicLong(1);

    @Override
    public void addUser(User user) throws UserLoginAlreadyUsedException, NullPointerException {
        if (user == null) {
            throw new NullPointerException();
        }
        Long newId = curId.getAndIncrement(); //jezeli juz istnieje uzytkownik o takim loginie to spalimy ID
        user.setId(newId);
        User existingUser = usersByLogin.putIfAbsent(user.getLogin(), user);
        if (existingUser != null) {
            throw new UserLoginAlreadyUsedException("User with login " + user.getLogin() + " already exists");
        }
        users.put(newId, user);
    }

    @Override
    public User getUser(Long id) throws NullPointerException {
        if (id == null) {
            throw new NullPointerException();
        }
        return users.get(id);
    }

    @Override
    public User getUserByLogin(String login) {
        if  (login == null) {
            return null;
        }
        return usersByLogin.get(login);  //TODO: nw czy to legalne, posiadanie dwoch tablic (jedna z ID druga z loginami)
    }                                    //jest git, tradeoff czasu za pamiec

    @Override
    public List<User> getUsersByLoginPattern(String loginPattern) { //tu mozna by bylo zrobic regexa, albo jakis wczesniej ustalony pattern ale w wymaganiach nieopisane :3
        if  (loginPattern == null) {
            return List.of();
        }
        return usersByLogin.values().stream()
                .filter(user -> user.getLogin().contains(loginPattern))  //imo tak git, zwrocic wszystko co zawiera
                .collect(Collectors.toList());
    }

//    @Override
//    public boolean activateUser(Long id) {
//        if (id == null) {
//            return false;
//        }
//        User user = users.get(id);
//        if (user == null) {
//            return false;
//        }
//        if(!user.isActive()) {
//            user.setActive(true);
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean deactivateUser(Long id) {
//        if (id == null) {
//            return false;
//        }
//        User user = users.get(id);
//        if (user == null) {
//            return false;
//        }
//        if(user.isActive()) {
//            user.setActive(false);
//            return true;
//        }
//        return false;
//    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public List<User> findAllActive() {
        return users.values().stream()
                .filter(User::isActive)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findAllUnactive() {
        return users.values().stream()
                .filter(user -> !user.isActive())
                .collect(Collectors.toList());
    }
}
