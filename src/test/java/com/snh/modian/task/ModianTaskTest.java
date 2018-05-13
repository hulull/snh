package com.snh.modian.task;

import com.snh.modian.SnhApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SnhApplication.class)
public class ModianTaskTest {
    @Autowired
    ModianTask modianTask;

    @Test
    public void test() {
//
//        modianTask.rollCardByRank(3, "test");
//        System.out.println(new Date(1525792738000L));
//        System.out.println(Math.random());
        modianTask.getUserRankDetail("喵星人");
    }
}
