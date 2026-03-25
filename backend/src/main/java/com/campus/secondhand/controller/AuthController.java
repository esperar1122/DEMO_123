package com.campus.secondhand.controller;

import com.campus.secondhand.common.AuthResponse;
import com.campus.secondhand.common.Result;
import com.campus.secondhand.dto.LoginRequest;
import com.campus.secondhand.dto.RegisterRequest;
import com.campus.secondhand.service.AuthService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public Result<AuthResponse> register(@RequestBody @Validated RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return Result.success(response);
        } catch (IllegalArgumentException e) {
            return Result.error(400, e.getMessage());
        } catch (Exception e) {
            return Result.error(500, "注册失败: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public Result<AuthResponse> login(@RequestBody @Validated LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return Result.success(response);
        } catch (IllegalArgumentException e) {
            return Result.error(400, e.getMessage());
        } catch (Exception e) {
            return Result.error(500, "登录失败: " + e.getMessage());
        }
    }
}