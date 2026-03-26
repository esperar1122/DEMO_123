package com.campus.secondhand.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.campus.secondhand.dto.MessageRequest;
import com.campus.secondhand.entity.Item;
import com.campus.secondhand.entity.Message;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.mapper.ItemMapper;
import com.campus.secondhand.mapper.MessageMapper;
import com.campus.secondhand.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageService {

    private final MessageMapper messageMapper;
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;
    private final NotificationService notificationService;

    public MessageService(MessageMapper messageMapper, UserMapper userMapper, ItemMapper itemMapper, NotificationService notificationService) {
        this.messageMapper = messageMapper;
        this.userMapper = userMapper;
        this.itemMapper = itemMapper;
        this.notificationService = notificationService;
    }

    @Transactional
    public Message createMessage(Long userId, MessageRequest request) {
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("留言内容不能为空");
        }
        if (request.getContent().length() > 500) {
            throw new IllegalArgumentException("留言内容不能超过500字");
        }

        Item item = itemMapper.selectById(request.getItemId());
        if (item == null || item.getDeleted() == 1) {
            throw new IllegalArgumentException("商品不存在");
        }

        User user = userMapper.selectById(userId);
        if (user == null || user.getStatus() == 1) {
            throw new IllegalArgumentException("用户不存在或已被封禁");
        }

        Message message = new Message();
        message.setItemId(request.getItemId());
        message.setUserId(userId);
        message.setContent(request.getContent().trim());
        messageMapper.insert(message);

        if (!item.getSellerId().equals(userId)) {
            notificationService.createNotification(
                item.getSellerId(),
                "message",
                "新消息提醒",
                "用户 " + user.getUsername() + " 对您的商品 " + item.getTitle() + " 发送了新消息",
                request.getItemId()
            );
        }

        return message;
    }

    public List<Message> getMessagesByItem(Long itemId) {
        List<Message> messages = messageMapper.selectList(
            new QueryWrapper<Message>()
                .eq("item_id", itemId)
                .eq("deleted", 0)
                .orderByAsc("created_at")
        );

        for (Message message : messages) {
            User user = userMapper.selectById(message.getUserId());
            if (user != null) {
                message.setUserId(user.getId());
            }
        }

        return messages;
    }
}