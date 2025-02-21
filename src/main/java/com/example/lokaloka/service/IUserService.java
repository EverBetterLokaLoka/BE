package com.example.lokaloka.service;

import org.springframework.security.core.Authentication;

public interface IUserService {
    String checkLoginStatus(Authentication authentication);
}
