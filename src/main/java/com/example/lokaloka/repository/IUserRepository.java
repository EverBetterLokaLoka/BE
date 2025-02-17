package com.example.lokaloka.repository;

import com.example.lokaloka.domain.dto.resdto.UserResDTO;
import com.example.lokaloka.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User,Long> {

}
