package com.campus.secondhand.controller;

import com.campus.secondhand.common.Result;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public Result<List<User>> getAllUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<User> users = userService.getAllUsers(page, size);
            return Result.success(users);
        } catch (Exception e) {
            return Result.error(500, "获取用户列表失败: " + e.getMessage());
        }
    }

    @PutMapping("/users/{id}/ban")
    public Result<Void> banUser(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        try {
            userService.banUser(id, currentUser.getId());
            return Result.success();
        } catch (IllegalArgumentException e) {
            return Result.error(400, e.getMessage());
        } catch (Exception e) {
            return Result.error(500, "封禁用户失败: " + e.getMessage());
        }
    }

    @PutMapping("/users/{id}/unban")
    public Result<Void> unbanUser(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        try {
            userService.unbanUser(id, currentUser.getId());
            return Result.success();
        } catch (IllegalArgumentException e) {
            return Result.error(400, e.getMessage());
        } catch (Exception e) {
            return Result.error(500, "解封用户失败: " + e.getMessage());
        }
    }

    @GetMapping("/dashboard")
    public Result<Object> getDashboard() {
        try {
            Object data = userService.getDashboardStats();
            return Result.success(data);
        } catch (Exception e) {
            return Result.error(500, "获取仪表盘数据失败: " + e.getMessage());
        }
    }
}