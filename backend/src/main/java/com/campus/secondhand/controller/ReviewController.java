package com.campus.secondhand.controller;

import com.campus.secondhand.common.Result;
import com.campus.secondhand.entity.Order;
import com.campus.secondhand.entity.Review;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.service.ReviewService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public Result<Review> createReview(
            @AuthenticationPrincipal User user,
            @RequestParam Long orderId,
            @RequestParam Integer rating,
            @RequestParam(required = false) String content) {
        try {
            Review review = reviewService.createReview(orderId, user.getId(), rating, content);
            return Result.success(review);
        } catch (IllegalArgumentException e) {
            return Result.error(400, e.getMessage());
        } catch (Exception e) {
            return Result.error(500, "创建评价失败: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public Result<List<Review>> getUserReviews(@PathVariable Long userId) {
        try {
            List<Review> reviews = reviewService.getReviewsByUser(userId);
            return Result.success(reviews);
        } catch (Exception e) {
            return Result.error(500, "获取评价列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/completable")
    public Result<List<Order>> getCompletableOrders(@AuthenticationPrincipal User user) {
        try {
            List<Order> orders = reviewService.getCompletableOrders(user.getId());
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error(500, "获取可评价订单失败: " + e.getMessage());
        }
    }
}