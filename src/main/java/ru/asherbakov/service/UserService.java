package ru.asherbakov.service;

import ru.asherbakov.models.User;

import java.util.Optional;

public interface UserService {
    User getUser();

    Iterable<User> getAllUsers();

    String getUserFullName(User user);
}
