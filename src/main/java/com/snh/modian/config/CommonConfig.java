package com.snh.modian.config;

import com.snh.modian.constant.RedisOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {
    @Bean
    RedisOperation<String> stringRedisOperation() {
        return new RedisOperation<>();
    }
}
