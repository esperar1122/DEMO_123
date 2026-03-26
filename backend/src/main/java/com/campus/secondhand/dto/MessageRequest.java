package com.campus.secondhand.dto;

import lombok.Data;

@Data
public class MessageRequest {
    private Long itemId;
    private String content;
}