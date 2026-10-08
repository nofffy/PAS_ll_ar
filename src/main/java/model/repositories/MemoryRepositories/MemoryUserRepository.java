package model.repositories.MemoryRepositories;

import model.User;
import model.repositories.UserRepository;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@ApplicationScoped
public class MemoryUserRepository implements UserRepository {

    private Map<Long, User> users = new HashMap<Long, User>();
    private Map<String, User> usersByLogin = new HashMap<String, User>();

    private Long curId = 1L;

    private ReadWriteLock lock = new ReentrantReadWriteLock(true);
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();
    private ReadWriteLock loginLock = new ReentrantReadWriteLock(true);
    private Lock loginReadLock = lock.readLock();
    private Lock loginWriteLock = lock.writeLock();


    @Override
    public boolean addUser(User user) {
        if (user == null) {
            return false;  //TODO: zmiana boolean na void (albo return user po dodaniu) i tego na wyjątki?
        }

        writeLock.lock();
        loginWriteLock.lock();
        try {
            if (usersByLogin.containsKey(user.getLogin())) {
                return false;
            }
            user.setId(curId++);
            users.put(user.getId(), user);
            usersByLogin.put(user.getLogin(), user);
            return true;
        } finally {
            writeLock.unlock();
            loginWriteLock.unlock(); //TODO: rozkminka czy sekcje krytyczne zagnieżdżone czy przenikające
        }
    }

    @Override
    public User getUser(Long id) {
        if  (id < 0 || id >= users.size()) {
            return null;
        }
        readLock.lock();
        try {
            return users.get(id);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public User getUserByLogin(String login) {
        if  (login == null) {
            return null;
        }
        loginReadLock.lock();
        try {
            return users.get(login);
        } finally {
            loginReadLock.unlock();  //TODO: nw czy to legalne, posiadanie dwoch tablic (jedna z ID druga z loginami)
        }
    }

    @Override
    public List<User> getUsersByLoginPattern(String loginPattern) { //tu mozna by bylo zrobic regexa, albo jakis wczesniej ustalony pattern ale w wymaganiach nieopisane :3
        if  (loginPattern == null) {
            return null;
        }
        loginReadLock.lock();
        try {
            return usersByLogin.values().stream()
                    .filter(user -> user.getLogin().contains(loginPattern))
                    .collect(Collectors.toList());
        } finally {
            loginReadLock.unlock();
        }
    }

    @Override
    public boolean removeUser(Long id) {
        if (id == null) {
            return false;
        }
        writeLock.lock();
        try {
            users.remove(id);
            return true;
        } finally {
            writeLock.unlock();
        }
    }


    @Override
    public List<User> getUsers() {
        readLock.lock();
        try {
            return new ArrayList<>(users.values());
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public List<User> findAllActive() {
        readLock.lock();
        try {
            return users.values().stream()
                    .filter(User::isActive)
                    .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public List<User> findAllUnactive() {
        readLock.lock();
        try {
            return users.values().stream()
                    .filter(user -> !user.isActive())
                    .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }
}
