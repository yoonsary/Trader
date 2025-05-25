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

    public Users(String username, String password, String nickname, String email) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.balance = 0;
    }

    public void addBalance(int amount) {
        this.balance += amount;
    }

    public void subtractBalance(int amount) {
        this.balance -= amount;
    }

}
