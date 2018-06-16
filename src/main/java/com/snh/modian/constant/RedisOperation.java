package com.snh.modian.constant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Map;
import java.util.concurrent.TimeUnit;


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

    public void setCacheObject(String key, T t) {
        ValueOperations<String, T> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, t, 1, TimeUnit.DAYS);
    }

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}
