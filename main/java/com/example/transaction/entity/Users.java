package com.example.transaction.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "users")
@ToString
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String username;
    private String password;
    private String nickname;
    private String email;
    private int balance;

    // balance는 초기값 0으로 시작하는 생성자
    public Users(String username, String password, String nickname, String email) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.balance = 0;
    }

    // 충전과 송금을 위한 메서드
    public void addBalance(int amount) {
        this.balance += amount;
    }

    public void subtractBalance(int amount) {
        this.balance -= amount;
    }

}
