package com.campus.secondhand.controller;

import com.campus.secondhand.common.Result;
import com.campus.secondhand.dto.MessageRequest;
import com.campus.secondhand.entity.Message;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.service.MessageService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final com.campus.secondhand.mapper.UserMapper userMapper;

    public MessageController(MessageService messageService, com.campus.secondhand.mapper.UserMapper userMapper) {
        this.messageService = messageService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public Result<Message> createMessage(
            @AuthenticationPrincipal User user,
            @RequestBody @Validated MessageRequest request) {
        try {
            Message message = messageService.createMessage(user.getId(), request);
            return Result.success(message);
        } catch (IllegalArgumentException e) {
            return Result.error(400, e.getMessage());
        } catch (Exception e) {
            return Result.error(500, "留言失败: " + e.getMessage());
        }
    }

    @GetMapping("/item/{itemId}")
    public Result<List<Map<String, Object>>> getMessages(@PathVariable Long itemId) {
        try {
            List<Message> messages = messageService.getMessagesByItem(itemId);
            List<Map<String, Object>> result = messages.stream().map(msg -> {
                User user = userMapper.selectById(msg.getUserId());
                Map<String, Object> map = new java.util.HashMap<>();
                map.put("id", msg.getId());
                map.put("content", msg.getContent());
                map.put("createdAt", msg.getCreatedAt());
                map.put("userId", msg.getUserId());
                map.put("username", user != null ? user.getUsername() : "未知用户");
                return map;
            }).collect(Collectors.toList());
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(500, "获取留言失败: " + e.getMessage());
        }
    }
}