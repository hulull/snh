package com.snh.modian.task;

import com.snh.modian.domain.modian.Order;
import com.snh.modian.service.RollCardServiceImpl;
import com.snh.modian.util.TimeUtils;
import com.snh.modian.util.modian.ModianApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class ModianTask implements Runnable {

    @Value("${MODIANID}")
    private int MODIANID;
    @Autowired
    RollCardServiceImpl rollCardService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ModianTask.class);
    private static Long newestClockIn = System.currentTimeMillis();
//    private static Long newestClockIn = 0L;

    public boolean modianTask() {
        List<Order> orderList = ModianApi.queryOrders(MODIANID, 1);
        if (CollectionUtils.isEmpty(orderList)) {
            return false;
        }
        List<Order> resultList = new ArrayList<>();
        Long maxPayTime = newestClockIn;
        for (int i = orderList.size()-1; i >= 0; i--) {
            Order order = orderList.get(i);
            if (TimeUtils.StringToLong(order.getPay_time()) > newestClockIn) {
                resultList.add(order);
            }
            maxPayTime = Math.max(TimeUtils.StringToLong(order.getPay_time()), maxPayTime);
        }
        newestClockIn = maxPayTime;
        if (CollectionUtils.isEmpty(resultList)) {
            return false;
        }
        rollCardService.rollCardV2(MODIANID, resultList);
        return true;
    }



    @Override
    public void run() {
        boolean result = false;
        while (true) {
            try {
                result = modianTask();
            } catch (Exception e) {
                LOGGER.error("modianTask error!", e);
            }
            if (!result) {
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
