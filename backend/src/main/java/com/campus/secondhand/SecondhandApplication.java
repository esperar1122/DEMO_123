package com.campus.secondhand;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.mapper.UserMapper;

@SpringBootApplication
public class SecondhandApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecondhandApplication.class, args);
    }

    @Bean
    public CommandLineRunner createAdmin(PasswordEncoder passwordEncoder, UserMapper userMapper) {
        return args -> {
            User existing = userMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User>()
                    .eq("username", "admin")
            );
            if (existing == null) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("a123456"));
                admin.setRole(1);
                admin.setCreditScore(100);
                admin.setStatus(0);
                userMapper.insert(admin);
                System.out.println("Admin account created: admin / a123456");
            } else {
                System.out.println("Admin account already exists");
            }
        };
    }
}