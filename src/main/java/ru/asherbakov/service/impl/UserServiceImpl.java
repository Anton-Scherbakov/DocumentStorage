package ru.asherbakov.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.asherbakov.models.User;
import ru.asherbakov.repository.UserRepository;
import ru.asherbakov.service.UserService;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName()).orElseThrow();
    }
    @Override
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
    @Override
    public String getUserFullName(User user) {
        String userFullName = String.format("%s %s", user.getLastName(), user.getFirstName());
        if (user.getMiddleName() != null && !user.getMiddleName().isBlank()) {
            userFullName += " " + user.getMiddleName();
        }
        return userFullName;
    }
}
