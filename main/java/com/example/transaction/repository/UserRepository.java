package com.example.transaction.repository;

import com.example.transaction.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    // 사용자 이름(아이디)으로 사용자 찾기
    Users findByUsername(String username);
}
