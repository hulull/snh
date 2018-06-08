package com.snh.modian.task;

import com.snh.modian.SnhApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SnhApplication.class)
public class ModianPkTaskTest {
    @Autowired
    ModianPkTask modianPkTask;

    @Test
    public void test() {
//        List<Integer> modianIdList = Arrays.asList(18302,18303,18304,18306,18309,18310,18311);
//        modianPkTask.modianPkTask(modianIdList);
        modianPkTask.run();
    }
}
