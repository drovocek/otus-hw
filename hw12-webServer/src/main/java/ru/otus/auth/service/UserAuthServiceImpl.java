package ru.otus.auth.service;

import ru.otus.auth.dao.UserDao;

public class UserAuthServiceImpl implements UserAuthService {

    private final UserDao userDao;

    public UserAuthServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return userDao.findByLogin(login)
                .map(user -> user.password().equals(password))
                .orElse(false);
    }
}
