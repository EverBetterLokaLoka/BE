package com.example.lokaloka.service.impl;

import com.example.lokaloka.domain.dto.reqdto.ProfileReqDTO;
import com.example.lokaloka.domain.dto.reqdto.UserReqDTO;
import com.example.lokaloka.domain.dto.resdto.UserResDTO;
import com.example.lokaloka.domain.entity.Image;
import com.example.lokaloka.exception.AppException;
import com.example.lokaloka.mapper.UserMapper;
import com.example.lokaloka.repository.IImageRepository;
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

    @Autowired
    private IImageRepository imageRepository;


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

        // Lấy ảnh avatar từ bảng images
        Image avatar = imageRepository.findByUserIdAndType(user.getId(), "avatar");

        UserResDTO userResDTO = userMapper.toUserResDTO(user);
        if (avatar != null) {
            userResDTO.setAvatar(avatar.getContent()); // Gán avatar vào DTO
        }
        return userMapper.toUserResDTO(user);
    }

    @Override
    @Transactional
    public ProfileReqDTO updateUser(Long id, ProfileReqDTO profileReqDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // ✅ Kiểm tra password không có khoảng trắng nếu có nhập
        if (profileReqDTO.getPassword() != null && profileReqDTO.getPassword().contains(" ")) {
            throw new AppException(ErrorCode.PASSWORD_CONTAIN_SPACE);
        }

        // ✅ Kiểm tra full name không phải chỉ chứa khoảng trắng
        if (profileReqDTO.getFull_name() != null && profileReqDTO.getFull_name().trim().isEmpty()) {
            throw new AppException(ErrorCode.FULL_NAME_IS_EMPTY);
        }

        // ✅ Kiểm tra số điện thoại có đúng 10 chữ số

        if (profileReqDTO.getEmergency_numbers() != null &&
                !profileReqDTO.getEmergency_numbers().isEmpty() &&
                !profileReqDTO.getEmergency_numbers().matches("\\d{10}")) {
            throw new AppException(ErrorCode.IN_VALID_EMERGENCY_NUMBER);
        }

        if (profileReqDTO.getPassword() != null && !profileReqDTO.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(profileReqDTO.getPassword());
            user.setPassword(encodedPassword);
        }
        if (profileReqDTO.getFull_name() != null) {
            user.setFull_name(profileReqDTO.getFull_name());
        }
        if (profileReqDTO.getAddress() != null) {
            user.setAddress(profileReqDTO.getAddress());
        }
        if (profileReqDTO.getPhone() != null) {
            user.setPhone(profileReqDTO.getPhone());
        }
        if (profileReqDTO.getGender() != null) {
            user.setGender(profileReqDTO.getGender());
        }
        if (profileReqDTO.getDob() != null) {
            user.setDob(profileReqDTO.getDob());
        }

        // ✅ Chỉ cập nhật emergency_number nếu có nhập vào
        if (profileReqDTO.getEmergency_numbers() != null) {
            user.setEmergency_numbers(profileReqDTO.getEmergency_numbers());
        }

        user.setUpdated_at(Timestamp.valueOf(LocalDateTime.now()));
        userRepository.save(user);

        return userMapper.toProfileReqDTO(user);
    }

}