package com.snh.modian.util;

import com.snh.modian.SnhApplication;
import com.snh.modian.constant.ModianConstant;
import com.snh.modian.domain.modian.Detail;
import com.snh.modian.domain.modian.Order;
import com.snh.modian.domain.modian.RankMoney;
import com.snh.modian.util.cqp.CqpHttpApi;
import com.snh.modian.util.modian.ModianApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SnhApplication.class)
public class ModianApiTest {

    @Value("${MODIANID}")
    private int MODIANID;
    @Value("${GROUPID}")
    private int GROUPID;

    @Test
    public void test() {
//        ModianApi.queryDetails(String.valueOf(MODIANID + "," + "17871"));
//        List<Order> orders = ModianApi.queryOrders(17871, 1);
//        System.out.println(1);
        ModianApi.queryDetail(20066);
//        ModianApi.queryRankMoneys(17871, 1);
    }
}
