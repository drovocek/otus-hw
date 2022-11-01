package ru.otus.auth.service;

public interface UserAuthService {

    boolean authenticate(String login, String password);
}
