package com.example.lokaloka.service.impl;

import com.example.lokaloka.domain.dto.reqdto.UserReqDTO;
import com.example.lokaloka.domain.dto.resdto.UserResDTO;
import com.example.lokaloka.exception.AppException;
import com.example.lokaloka.mapper.UserMapper;
import com.example.lokaloka.service.IUserService;
import com.example.lokaloka.util.ErrorCode;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.lokaloka.domain.entity.User;
import com.example.lokaloka.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


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

    @Override
    public UserResDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return userMapper.toUserResDTO(user);
    }

    @Override
    @Transactional
    public UserResDTO updateUser(Long id, UserReqDTO userReqDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Không cho phép thay đổi email
        userReqDTO.setEmail(user.getEmail());

        // Nếu có mật khẩu mới, mã hóa trước khi cập nhật
        if (userReqDTO.getPassword() != null && !userReqDTO.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(userReqDTO.getPassword());
            user.setPassword(encodedPassword);
        }

        // Chỉ cập nhật những trường có giá trị mới
        if (userReqDTO.getFullName() != null) {
            user.setFull_name(userReqDTO.getFullName());
        }
        if (userReqDTO.getAddress() != null) {
            user.setAddress(userReqDTO.getAddress());
        }
        if (userReqDTO.getPhone() != null) {
            user.setPhone(userReqDTO.getPhone());
        }
        if (userReqDTO.getGender() != null) {
            user.setGender(userReqDTO.getGender());
        }
        if (userReqDTO.getDob() != null) {
            user.setDob(userReqDTO.getDob());
        }

        // 🕒 Cập nhật thời gian `updated_at`
        user.setUpdated_at(Timestamp.valueOf(LocalDateTime.now()));

        // Lưu dữ liệu cập nhật vào database
        userRepository.save(user);

        // Trả về UserResDTO sau khi cập nhật
        return userMapper.toUserResDTO(user);
    }




}