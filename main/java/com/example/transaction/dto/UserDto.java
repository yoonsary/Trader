package com.example.transaction.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {

    private String username;
    private String password;
    private String nickname;
    private String email;
}
