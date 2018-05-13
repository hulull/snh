package com.snh.modian.task;

import com.snh.modian.SnhApplication;
import com.snh.modian.util.cqp.CqpHttpApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SnhApplication.class)
public class ModianTaskTest {
    @Autowired
    ModianTask modianTask;
    private static final Logger LOGGER = LoggerFactory.getLogger(ModianTaskTest.class);

    @Test
    public void test() {
//
//        modianTask.rollCardByRank(3, "test");
//        modianTask.getUserRankDetail("喵星人");
        List<String> rcard = modianTask.rCard;
        String card = rcard.get(rcard.size()-1);
        String file = "level3/" + card + ".png";
        String info = "[CQ:image,file=" + file + "]";
    }

    @Test
    public void logTest() {
        LOGGER.info("log");
    }
}
