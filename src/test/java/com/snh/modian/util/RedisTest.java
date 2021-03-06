package com.snh.modian.util;

import com.snh.modian.SnhApplication;
import com.snh.modian.constant.RedisOperation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SnhApplication.class)
public class RedisTest {
    @Autowired
    RedisOperation<String> redisOperation;
    @Test
    public void testRedis() {
        long ret = redisOperation.incrementHash("testhash", "testhash1");
        System.out.println(ret);
    }

    @Test
    public void testGet() {
        Map<String, String> ret = redisOperation.getAllHashValue("testhash");
        System.out.println(ret);
    }
}
