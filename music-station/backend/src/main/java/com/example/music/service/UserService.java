package com.example.music.service;

import com.example.music.dto.AuthResponse;
import com.example.music.dto.LoginRequest;
import com.example.music.dto.RegisterRequest;
import com.example.music.entity.Role;
import com.example.music.entity.User;
import com.example.music.repository.UserRepository;
import com.example.music.security.Sha256Util;
import com.example.music.security.TokenStore;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final TokenStore tokenStore;

    public UserService(UserRepository userRepository, TokenStore tokenStore) {
        this.userRepository = userRepository;
        this.tokenStore = tokenStore;
    }

    public AuthResponse register(RegisterRequest request) {
        userRepository.findByUsername(request.getUsername()).ifPresent(user -> {
            throw new IllegalArgumentException("用户名已存在");
        });
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(Sha256Util.sha256(request.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        String token = tokenStore.createToken(user.getUsername(), user.getRole());
        return new AuthResponse(token, user.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        if (!user.getPassword().equals(Sha256Util.sha256(request.getPassword()))) {
            throw new IllegalArgumentException("密码错误");
        }
        String token = tokenStore.createToken(user.getUsername(), user.getRole());
        return new AuthResponse(token, user.getRole().name());
    }
}
