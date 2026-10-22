package model.repositories.MemoryRepositories;

import Exeptions.ResourceNotFoundException;
import Exeptions.UserLoginAlreadyUsedException;
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
    public void addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Can't be null");
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
    public User getUser(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        return users.get(id);
    }

    @Override
    public User getUserByLogin(String login) {
        if (login == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        return usersByLogin.get(login);  //TODO: nw czy to legalne, posiadanie dwoch tablic (jedna z ID druga z loginami)
    }                                    //jest git, tradeoff czasu za pamiec

    @Override
    public List<User> getUsersByLoginPattern(String loginPattern) { //tu mozna by bylo zrobic regexa, albo jakis wczesniej ustalony pattern ale w wymaganiach nieopisane :3
        if  (loginPattern == null) {
            return List.of();
        }
        final String lp = loginPattern.toLowerCase();
        return usersByLogin.values().stream()
                .filter(user -> user.getLogin().toLowerCase().contains(lp))  //imo tak git, zwrocic wszystko co zawiera
                .collect(Collectors.toList());
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
