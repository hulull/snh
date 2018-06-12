package com.snh.modian.constant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;


public class RedisOperation<T> {
    @Autowired
    RedisTemplate redisTemplate;

    public long incrementHash(String key, String hashKey) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        return hashOperations.increment(key, hashKey, 1);
    }

    public Map<String, String> getAllHashValue(String key) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(key);
    }
}
