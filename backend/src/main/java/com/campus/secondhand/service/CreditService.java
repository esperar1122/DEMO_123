package com.campus.secondhand.service;

import com.campus.secondhand.entity.User;
import com.campus.secondhand.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class CreditService {

    private final UserMapper userMapper;

    public CreditService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public void updateCredit(Long userId, int delta) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return;
        }

        int newScore = user.getCreditScore() + delta;
        if (newScore > 100) newScore = 100;
        if (newScore < 0) newScore = 0;

        user.setCreditScore(newScore);
        userMapper.updateById(user);
    }

    public String getCreditLevel(int score) {
        if (score >= 90) return "极好";
        if (score >= 80) return "优秀";
        if (score >= 70) return "良好";
        if (score >= 60) return "一般";
        return "糟糕";
    }
}