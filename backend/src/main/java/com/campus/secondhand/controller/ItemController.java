package com.campus.secondhand.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.secondhand.common.Result;
import com.campus.secondhand.dto.ItemCreateRequest;
import com.campus.secondhand.entity.Item;
import com.campus.secondhand.entity.ItemImage;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.service.ItemService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public Result<Item> createItem(
            @AuthenticationPrincipal User user,
            @RequestBody @Validated ItemCreateRequest request) {
        try {
            Item item = itemService.createItem(user.getId(), request);
            return Result.success(item);
        } catch (IllegalArgumentException e) {
            return Result.error(400, e.getMessage());
        } catch (Exception e) {
            return Result.error(500, "创建商品失败: " + e.getMessage());
        }
    }

    @GetMapping
    public Result<Map<String, Object>> getItems(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        try {
            Page<Item> items = itemService.getActiveItems(page, size, keyword);
            Map<String, Object> result = new HashMap<>();
            result.put("records", items.getRecords());
            result.put("total", items.getTotal());
            result.put("pages", items.getPages());
            result.put("current", items.getCurrent());
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(500, "获取商品列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<Map<String, Object>> getItemDetail(@PathVariable Long id) {
        try {
            Item item = itemService.getItemById(id);
            if (item == null) {
                return Result.error(404, "商品不存在");
            }
            List<ItemImage> images = itemService.getItemImages(id);
            Map<String, Object> result = new HashMap<>();
            result.put("item", item);
            result.put("images", images);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(500, "获取商品详情失败: " + e.getMessage());
        }
    }

    @GetMapping("/my")
    public Result<Map<String, Object>> getMyItems(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Item> items = itemService.getItemsBySeller(user.getId(), page, size);
            Map<String, Object> result = new HashMap<>();
            result.put("records", items.getRecords());
            result.put("total", items.getTotal());
            result.put("pages", items.getPages());
            result.put("current", items.getCurrent());
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(500, "获取我的商品失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<Item> updateItem(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @RequestBody ItemCreateRequest request) {
        try {
            Item item = itemService.updateItem(id, user.getId(), request);
            return Result.success(item);
        } catch (IllegalArgumentException e) {
            return Result.error(400, e.getMessage());
        } catch (Exception e) {
            return Result.error(500, "更新商品失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/offline")
    public Result<Void> offlineItem(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        try {
            itemService.offlineItem(id, user.getId());
            return Result.success();
        } catch (IllegalArgumentException e) {
            return Result.error(400, e.getMessage());
        } catch (Exception e) {
            return Result.error(500, "下架商品失败: " + e.getMessage());
        }
    }
}