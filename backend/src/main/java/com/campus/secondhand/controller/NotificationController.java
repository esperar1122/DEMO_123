package com.campus.secondhand.controller;

import com.campus.secondhand.common.Result;
import com.campus.secondhand.entity.Notification;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.service.NotificationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public Result<Map<String, Object>> getNotifications(@AuthenticationPrincipal User user) {
        try {
            List<Notification> notifications = notificationService.getUserNotifications(user.getId());
            Long unreadCount = notificationService.getUnreadCount(user.getId());
            
            Map<String, Object> result = new HashMap<>();
            result.put("notifications", notifications);
            result.put("unreadCount", unreadCount);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(500, "获取通知失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/read")
    public Result<Void> markAsRead(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        try {
            notificationService.markAsRead(id, user.getId());
            return Result.success();
        } catch (Exception e) {
            return Result.error(500, "标记已读失败: " + e.getMessage());
        }
    }

    @PutMapping("/read-all")
    public Result<Void> markAllAsRead(@AuthenticationPrincipal User user) {
        try {
            notificationService.markAllAsRead(user.getId());
            return Result.success();
        } catch (Exception e) {
            return Result.error(500, "标记全部已读失败: " + e.getMessage());
        }
    }

    @GetMapping("/unread-count")
    public Result<Map<String, Object>> getUnreadCount(@AuthenticationPrincipal User user) {
        try {
            Long count = notificationService.getUnreadCount(user.getId());
            Map<String, Object> result = new HashMap<>();
            result.put("count", count);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(500, "获取未读数量失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteNotification(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        try {
            notificationService.deleteNotification(id, user.getId());
            return Result.success();
        } catch (Exception e) {
            return Result.error(500, "删除通知失败: " + e.getMessage());
        }
    }
}