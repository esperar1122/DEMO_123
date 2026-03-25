package com.campus.secondhand.controller;

import com.campus.secondhand.common.Result;
import com.campus.secondhand.dto.OrderCreateRequest;
import com.campus.secondhand.entity.Item;
import com.campus.secondhand.entity.ItemImage;
import com.campus.secondhand.entity.Order;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.service.ItemService;
import com.campus.secondhand.service.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final ItemService itemService;

    public OrderController(OrderService orderService, ItemService itemService) {
        this.orderService = orderService;
        this.itemService = itemService;
    }

    @PostMapping
    public Result<Order> createOrder(
            @AuthenticationPrincipal User user,
            @RequestBody @Validated OrderCreateRequest request) {
        try {
            Order order = orderService.createOrder(user.getId(), request);
            return Result.success(order);
        } catch (IllegalArgumentException e) {
            return Result.error(400, e.getMessage());
        } catch (Exception e) {
            return Result.error(500, "创建订单失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<Map<String, Object>> getOrderDetail(@PathVariable Long id) {
        try {
            Order order = orderService.getOrderById(id);
            if (order == null) {
                return Result.error(404, "订单不存在");
            }
            Item item = itemService.getItemById(order.getItemId());
            List<ItemImage> images = itemService.getItemImages(order.getItemId());
            Map<String, Object> result = new HashMap<>();
            result.put("order", order);
            result.put("item", item);
            result.put("images", images);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(500, "获取订单详情失败: " + e.getMessage());
        }
    }

    @GetMapping("/buyer")
    public Result<List<Order>> getMyBuyOrders(@AuthenticationPrincipal User user) {
        try {
            List<Order> orders = orderService.getBuyerOrders(user.getId());
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error(500, "获取订单列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/seller")
    public Result<List<Order>> getMySellOrders(@AuthenticationPrincipal User user) {
        try {
            List<Order> orders = orderService.getSellerOrders(user.getId());
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error(500, "获取订单列表失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/confirm")
    public Result<Order> confirmOrder(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        try {
            Order order = orderService.confirmOrder(id, user.getId());
            return Result.success(order);
        } catch (IllegalArgumentException e) {
            return Result.error(400, e.getMessage());
        } catch (Exception e) {
            return Result.error(500, "确认订单失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/reject")
    public Result<Order> rejectOrder(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        try {
            Order order = orderService.rejectOrder(id, user.getId());
            return Result.success(order);
        } catch (IllegalArgumentException e) {
            return Result.error(400, e.getMessage());
        } catch (Exception e) {
            return Result.error(500, "拒绝订单失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/cancel")
    public Result<Order> cancelOrder(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        try {
            Order order = orderService.cancelOrder(id, user.getId());
            return Result.success(order);
        } catch (IllegalArgumentException e) {
            return Result.error(400, e.getMessage());
        } catch (Exception e) {
            return Result.error(500, "取消订单失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/complete")
    public Result<Order> completeOrder(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        try {
            Order order = orderService.completeOrder(id, user.getId());
            return Result.success(order);
        } catch (IllegalArgumentException e) {
            return Result.error(400, e.getMessage());
        } catch (Exception e) {
            return Result.error(500, "确认收货失败: " + e.getMessage());
        }
    }
}