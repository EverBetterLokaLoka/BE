package com.example.lokaloka.service;

import com.example.lokaloka.domain.dto.reqdto.UserReqDTO;
import com.example.lokaloka.domain.dto.resdto.UserResDTO;
import org.springframework.security.core.Authentication;

public interface IUserService {
    String checkLoginStatus(Authentication authentication);
    UserResDTO getUserById(Long id);
    UserResDTO updateUser(Long id, UserReqDTO userReqDTO);
}
