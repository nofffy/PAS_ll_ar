package db.repositories.Memory;

import Exeptions.ResourceNotFoundException;
import Exeptions.UserLoginAlreadyUsedException;
import jakarta.enterprise.inject.Typed;
import model.User;
import model.repositories.UserRepository;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@ApplicationScoped
@Typed(MemoryUserRepository.class)
public class MemoryUserRepository implements UserRepository {
    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final Map<String, User> usersByLogin = new ConcurrentHashMap<>();

    public MemoryUserRepository() {}

    @Override
    public void addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        String newId = UUID.randomUUID().toString();
        user.setId(newId);
        User existingUser = usersByLogin.putIfAbsent(user.getLogin(), user);
        if (existingUser != null) {
            throw new UserLoginAlreadyUsedException("User with login " + user.getLogin() + " already exists");
        }
        users.put(newId, user);
    }

    @Override
    public User getUser(String id) {
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
            throw new IllegalArgumentException("Can't be null");
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
    public void updateUser(User user) {
        if (user == null) throw new IllegalArgumentException("Can't be null");
        User old = users.get(user.getId());
        if (old == null) {
            throw new ResourceNotFoundException("User with id " + user.getId() + " does not exist");
        }
        if (!old.getLogin().equals(user.getLogin())) {
            User conflict = usersByLogin.putIfAbsent(user.getLogin(), user);
            if (conflict != null && !conflict.getId().equals(user.getId())) {
                throw new UserLoginAlreadyUsedException("User with login " + user.getLogin() + " already exists");
            }
            usersByLogin.remove(old.getLogin(), old);
        }
        users.put(user.getId(), user);
    }

    @Override
    public void setActive(String id, boolean active) {
        if (id == null) throw new IllegalArgumentException("Can't be null");
        User user = users.get(id);
        if (user == null) throw new ResourceNotFoundException("User with id " + id + " not found");
        user.setActive(active);
    }

    @Override
    public List<User> findAllActive() {
        return users.values().stream()
                .filter(User::isActive)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findAllInactive() {
        return users.values().stream()
                .filter(user -> !user.isActive())
                .collect(Collectors.toList());
    }
}
