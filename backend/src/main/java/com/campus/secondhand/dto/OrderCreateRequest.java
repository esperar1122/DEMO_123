package com.campus.secondhand.dto;

import lombok.Data;

@Data
public class OrderCreateRequest {
    private Long itemId;
    private Integer transactionType;
    private String deliveryAddress;
}