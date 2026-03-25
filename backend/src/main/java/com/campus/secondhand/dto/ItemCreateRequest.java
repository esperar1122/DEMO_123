package com.campus.secondhand.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ItemCreateRequest {
    private String title;
    private String description;
    private BigDecimal price;
    private List<String> images;
}