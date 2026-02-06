package com.example.music.controller;

import com.example.music.dto.AuthResponse;
import com.example.music.dto.LoginRequest;
import com.example.music.dto.RegisterRequest;
import com.example.music.dto.Result;
import com.example.music.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Result<AuthResponse> register(@RequestBody RegisterRequest request) {
        return Result.ok(userService.register(request));
    }

    @PostMapping("/login")
    public Result<AuthResponse> login(@RequestBody LoginRequest request) {
        return Result.ok(userService.login(request));
    }
}
