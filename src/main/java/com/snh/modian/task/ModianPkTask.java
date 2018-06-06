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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class ModianPkTask implements Runnable {

    @Value("${UNIT_MODIANIDS}")
    private String UNIT_MODIANIDS;
    @Value("${ROLL_CARD}")
    private String ROLL_CARD;
    @Autowired
    RollCardServiceImpl rollCardService;
    @Autowired
    ModianTask modianTask;

    private static final Logger LOGGER = LoggerFactory.getLogger(ModianPkTask.class);
    private static Long newestClockIn = System.currentTimeMillis();

    public boolean modianPkTask(List<Integer> modianIdList) {
        boolean flag = false;
        long maxPayTime = newestClockIn;
        for (Integer id : modianIdList) {
            List<Order> orders = ModianApi.queryOrders(id, 1);
            if (CollectionUtils.isEmpty(orders)) {
                continue;
            }
            List<Order> resultList = new ArrayList<>();
            for (Order order : orders) {
                if (TimeUtils.StringToLong(order.getPay_time()) > newestClockIn) {
                    resultList.add(order);
                    maxPayTime = Math.max(maxPayTime, TimeUtils.StringToLong(order.getPay_time()));
                }
            }
            if (!CollectionUtils.isEmpty(resultList)) {
                flag = true;
                rollCardService.rollCardV2(id, resultList);
            }
        }
        if (maxPayTime > newestClockIn) {
            LOGGER.info("newestClockIn:{} maxPayTime:{}", new Date(newestClockIn), new Date(maxPayTime));
            newestClockIn = maxPayTime;
        }
        return flag;
    }


    @Override
    public void run() {
        String unitIds = UNIT_MODIANIDS;
        String rollCard = ROLL_CARD;
        String[] ids = unitIds.split(",");
        String[] isRolls = rollCard.split(",");
        if (ids.length != isRolls.length) {
            LOGGER.error("pk settings error! {} {}", ids, isRolls);
            return;
        }
        List<Integer> modianIdList = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            try {
                if (isRolls[i].equalsIgnoreCase("yes")) {
                    modianIdList.add(Integer.valueOf(ids[i]));
                }
            } catch (NumberFormatException e) {
                LOGGER.error("pk settings error! {}", ids[i]);
                return;
            }
        }
        if (CollectionUtils.isEmpty(modianIdList)) {
            LOGGER.error("pk settings error! {} {}", ids, isRolls);
            return;
        }
        while (true) {
            try {
                boolean ret = modianPkTask(modianIdList);
                if (!ret) {
                    TimeUnit.SECONDS.sleep(10);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                LOGGER.error("modianPkTask error!", e);
            }
        }
    }
}
