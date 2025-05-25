package com.example.transaction.service;

import com.example.transaction.dto.UserDto;
import com.example.transaction.entity.Users;
import com.example.transaction.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(UserDto userDto) {
        Users user = new Users(userDto.getUsername(), userDto.getPassword(), userDto.getNickname(), userDto.getEmail());
        userRepository.save(user);
    }

    public Users authenticate(String username, String password) {
        Users user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public Users charge(Long userId, int amount) {
        Optional<Users> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            user.addBalance(amount);
            userRepository.save(user);
            return user;
        }
        return null;
    }

    public Users exchange(Long userId, int amount) {
        Optional<Users> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();

            if (amount > 0 && user.getBalance() >= amount) {
                user.subtractBalance(amount);
                userRepository.save(user);
                return user;
            }
        }
        return null;
    }
}
