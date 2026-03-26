package com.campus.secondhand.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.campus.secondhand.entity.Notification;
import com.campus.secondhand.mapper.NotificationMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationMapper notificationMapper;

    public NotificationService(NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }

    public void createNotification(Long userId, String type, String title, String content, Long relatedId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setRelatedId(relatedId);
        notification.setIsRead(0);
        notificationMapper.insert(notification);
    }

    public List<Notification> getUserNotifications(Long userId) {
        return notificationMapper.selectList(
            new QueryWrapper<Notification>()
                .eq("user_id", userId)
                .eq("deleted", 0)
                .orderByDesc("created_at")
        );
    }

    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationMapper.selectList(
            new QueryWrapper<Notification>()
                .eq("user_id", userId)
                .eq("is_read", 0)
                .eq("deleted", 0)
                .orderByDesc("created_at")
        );
    }

    public Long getUnreadCount(Long userId) {
        return notificationMapper.selectCount(
            new QueryWrapper<Notification>()
                .eq("user_id", userId)
                .eq("is_read", 0)
                .eq("deleted", 0)
        );
    }

    public void markAsRead(Long notificationId, Long userId) {
        Notification notification = notificationMapper.selectById(notificationId);
        if (notification != null && notification.getUserId().equals(userId)) {
            notification.setIsRead(1);
            notificationMapper.updateById(notification);
        }
    }

    public void markAllAsRead(Long userId) {
        List<Notification> notifications = notificationMapper.selectList(
            new QueryWrapper<Notification>()
                .eq("user_id", userId)
                .eq("is_read", 0)
                .eq("deleted", 0)
        );
        for (Notification notification : notifications) {
            notification.setIsRead(1);
            notificationMapper.updateById(notification);
        }
    }

    public void deleteNotification(Long notificationId, Long userId) {
        Notification notification = notificationMapper.selectById(notificationId);
        if (notification != null && notification.getUserId().equals(userId)) {
            notification.setDeleted(1);
            notificationMapper.updateById(notification);
        }
    }
}