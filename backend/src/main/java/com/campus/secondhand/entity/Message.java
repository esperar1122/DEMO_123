package com.campus.secondhand.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("messages")
public class Message {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long itemId;

    private Long userId;

    private String content;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableLogic
    private Integer deleted;
}