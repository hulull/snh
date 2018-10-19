package com.snh.modian.service;

import com.snh.modian.SnhApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SnhApplication.class)
public class QueryCardServiceImplTest {
    @Autowired
    QueryCardServiceImpl queryCardService;

    @Test
    public void test() {
        queryCardService.sendCardMsgByUsername("", 1L);
    }
}
