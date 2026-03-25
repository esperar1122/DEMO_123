package com.campus.secondhand.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.campus.secondhand.dto.OrderCreateRequest;
import com.campus.secondhand.entity.Item;
import com.campus.secondhand.entity.Order;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.mapper.ItemMapper;
import com.campus.secondhand.mapper.OrderMapper;
import com.campus.secondhand.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderMapper orderMapper;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;
    private final CreditService creditService;

    public OrderService(OrderMapper orderMapper, ItemMapper itemMapper, UserMapper userMapper, CreditService creditService) {
        this.orderMapper = orderMapper;
        this.itemMapper = itemMapper;
        this.userMapper = userMapper;
        this.creditService = creditService;
    }

    @Transactional
    public Order createOrder(Long buyerId, OrderCreateRequest request) {
        Item item = itemMapper.selectById(request.getItemId());
        if (item == null || item.getDeleted() == 1) {
            throw new IllegalArgumentException("商品不存在");
        }
        if (item.getStatus() != 0) {
            throw new IllegalArgumentException("商品不在售");
        }
        if (item.getSellerId().equals(buyerId)) {
            throw new IllegalArgumentException("不能购买自己的商品");
        }

        User buyer = userMapper.selectById(buyerId);
        if (buyer == null || buyer.getStatus() == 1) {
            throw new IllegalArgumentException("用户不存在或已被封禁");
        }

        if (request.getTransactionType() == 2 && (request.getDeliveryAddress() == null || request.getDeliveryAddress().isEmpty())) {
            throw new IllegalArgumentException("送货上门需填写地址");
        }

        item.setStatus(1);
        itemMapper.updateById(item);

        Order order = new Order();
        order.setItemId(item.getId());
        order.setBuyerId(buyerId);
        order.setTransactionType(request.getTransactionType());
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setTotalPrice(item.getPrice());
        order.setStatus(0);
        orderMapper.insert(order);

        return order;
    }

    public Order getOrderById(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || order.getDeleted() == 1) {
            return null;
        }
        return order;
    }

    public List<Order> getBuyerOrders(Long buyerId) {
        return orderMapper.selectList(
            new QueryWrapper<Order>()
                .eq("buyer_id", buyerId)
                .eq("deleted", 0)
                .orderByDesc("created_at")
        );
    }

    public List<Order> getSellerOrders(Long sellerId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.exists(
            "SELECT 1 FROM items WHERE items.id = orders.item_id AND items.seller_id = " + sellerId
        ).eq("deleted", 0).orderByDesc("created_at");
        return orderMapper.selectList(wrapper);
    }

    @Transactional
    public Order confirmOrder(Long orderId, Long userId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || order.getDeleted() == 1) {
            throw new IllegalArgumentException("订单不存在");
        }

        Item item = itemMapper.selectById(order.getItemId());
        if (!item.getSellerId().equals(userId)) {
            throw new IllegalArgumentException("无权限操作");
        }
        if (order.getStatus() != 0) {
            throw new IllegalArgumentException("该状态下无法确认");
        }

        order.setStatus(1);
        orderMapper.updateById(order);

        return order;
    }

    @Transactional
    public Order rejectOrder(Long orderId, Long userId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || order.getDeleted() == 1) {
            throw new IllegalArgumentException("订单不存在");
        }

        Item item = itemMapper.selectById(order.getItemId());
        if (!item.getSellerId().equals(userId)) {
            throw new IllegalArgumentException("无权限操作");
        }
        if (order.getStatus() != 0) {
            throw new IllegalArgumentException("该状态下无法拒绝");
        }

        item.setStatus(0);
        itemMapper.updateById(item);

        order.setStatus(3);
        orderMapper.updateById(order);

        creditService.updateCredit(item.getSellerId(), -3);

        return order;
    }

    @Transactional
    public Order cancelOrder(Long orderId, Long userId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || order.getDeleted() == 1) {
            throw new IllegalArgumentException("订单不存在");
        }

        if (!order.getBuyerId().equals(userId)) {
            throw new IllegalArgumentException("无权限操作");
        }
        if (order.getStatus() != 0) {
            throw new IllegalArgumentException("该状态下无法取消");
        }

        Item item = itemMapper.selectById(order.getItemId());
        item.setStatus(0);
        itemMapper.updateById(item);

        order.setStatus(3);
        orderMapper.updateById(order);

        creditService.updateCredit(order.getBuyerId(), -3);

        return order;
    }

    @Transactional
    public Order completeOrder(Long orderId, Long userId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || order.getDeleted() == 1) {
            throw new IllegalArgumentException("订单不存在");
        }

        if (!order.getBuyerId().equals(userId)) {
            throw new IllegalArgumentException("无权限操作");
        }
        if (order.getStatus() != 1) {
            throw new IllegalArgumentException("该状态下无法确认收货");
        }

        Item item = itemMapper.selectById(order.getItemId());
        item.setStatus(2);
        itemMapper.updateById(item);

        order.setStatus(2);
        orderMapper.updateById(order);

        creditService.updateCredit(item.getSellerId(), 2);
        creditService.updateCredit(order.getBuyerId(), 2);

        return order;
    }
}