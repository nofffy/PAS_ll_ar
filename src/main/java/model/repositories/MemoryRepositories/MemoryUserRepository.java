package model.repositories.MemoryRepositories;

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
    public synchronized boolean addUser(User user) {
        if (user == null) {
            return false;  //TODO: zmiana boolean na void (albo return user po dodaniu) i tego na wyjątki?
        }
        if (usersByLogin.containsKey(user.getLogin())) {
            return false;
        }
        Long newId = curId.getAndIncrement();
        user.setId(newId);
        users.put(newId, user);
        usersByLogin.put(user.getLogin(), user);
        return true;
    }

    @Override
    public User getUser(Long id) {
        if (id == null) {
            return null;
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

    @Override
    public boolean activateUser(Long id) {
        if (id == null) {
            return false;
        }
        User user = users.get(id);
        if (user == null) {
            return false;
        }
        if(!user.isActive()) {
            user.setActive(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean deactivateUser(Long id) {
        if (id == null) {
            return false;
        }
        User user = users.get(id);
        if (user == null) {
            return false;
        }
        if(user.isActive()) {
            user.setActive(false);
            return true;
        }
        return false;
    }

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
