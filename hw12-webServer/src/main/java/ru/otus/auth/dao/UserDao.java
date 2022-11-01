package ru.otus.auth.dao;

import ru.otus.auth.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    List<User> findAll();

    Optional<User> findByLogin(String login);
}