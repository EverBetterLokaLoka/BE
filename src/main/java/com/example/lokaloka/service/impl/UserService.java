package com.example.lokaloka.service.impl;

import com.example.lokaloka.service.IUserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.example.lokaloka.domain.entity.User;
import com.example.lokaloka.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public String checkLoginStatus(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            return userRepository.findByEmail(username)
                    .map(user -> "User " + user.getFull_name() + " is logged in.")
                    .orElse("User is authenticated but not found in database");
        }
        return "User is not logged in.";
    }
}