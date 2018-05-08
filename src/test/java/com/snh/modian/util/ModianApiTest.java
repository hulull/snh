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
//        Detail detail = ModianApi.queryDetail(MODIANID);
////        List<Order> orders = ModianApi.queryOrders(ModianConstant.ITEM_ID, 1);
//        List<RankMoney> rankMoneyList = new ArrayList<>();
//        int page = 1;
//        List<RankMoney> pageRankMoneyList = null;
//        do {
//            pageRankMoneyList = ModianApi.queryRankMoneys(MODIANID, page);
//            rankMoneyList.addAll(pageRankMoneyList);
//            page++;
//        } while (!CollectionUtils.isEmpty(pageRankMoneyList));
        Map<String, String> map = new HashMap<>();
        map.put("test1", "11");
        map.put("test2", "22");
        System.out.println(map.toString());
//        System.out.println(detail.toString());
//        CqpHttpApi.getInstance().sendGroupMsg(767485026, detail.toString());
    }
}
