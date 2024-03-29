package ru.otus.auth.dao;

import ru.otus.auth.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserDao implements UserDao {

    private final Map<Long, User> users;

    public InMemoryUserDao() {
        users = new HashMap<>();
        users.put(1L, new User(1L, "Крис Гир", "user1", "11111"));
        users.put(2L, new User(2L, "Ая Кэш", "user2", "11111"));
        users.put(3L, new User(3L, "Десмин Боргес", "user3", "11111"));
        users.put(4L, new User(4L, "Кетер Донохью", "user4", "11111"));
        users.put(5L, new User(5L, "Стивен Шнайдер", "user5", "11111"));
        users.put(6L, new User(6L, "Джанет Вэрни", "user6", "11111"));
        users.put(7L, new User(7L, "Брэндон Смит", "user7", "11111"));
    }

    @Override
    public List<User> findAll() {
        return users.values().stream().toList();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return users.values().stream().filter(v -> v.login().equals(login)).findFirst();
    }
}
