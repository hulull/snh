package com.snh.modian.task;

import com.snh.modian.domain.cqp.CqpHttpApiResp;
import com.snh.modian.domain.modian.Detail;
import com.snh.modian.util.TimeUtils;
import com.snh.modian.util.cqp.CqpHttpApi;
import com.snh.modian.util.modian.ModianApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Component
public class PkBroadcastTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(PkBroadcastTask.class);

    @Value("${UNIT_MODIANIDS}")
    private String UNIT_MODIANIDS;
    @Value("${GROUPID}")
    private int GROUPID;

    @Scheduled(cron = "0 0 * * * ?")
    public void pkBroadcast() {
        long time = System.currentTimeMillis();
        if (time > TimeUtils.getZeroClock() && time < TimeUtils.getSevenClock()) {
            return;
        }
        List<Detail> detailList = ModianApi.queryDetails(UNIT_MODIANIDS);
        if (CollectionUtils.isEmpty(detailList)) {
            LOGGER.error("ModianApi.queryDetails({}) failed!", UNIT_MODIANIDS);
            return;
        }
        Collections.sort(detailList);
        StringBuilder info = new StringBuilder("当前排名如下:\n====================\n");
        for (int i = 0; i < detailList.size(); i++) {
            Detail detail = detailList.get(i);
            int ranking = i + 1;
            info.append("【").append(ranking).append("】");
            info.append(detail.getPro_name()).append("\n");
            info.append("￥").append(detail.getAlready_raised()).append("/").append("￥").append(detail.getGoal()).append("\n");
//            info.append("https://zhongchou.modian.com/item/").append(detail.getPro_id()).append(".html\n");
            info.append("====================\n");
        }
        for (int i = 0; i <= 2; i++) {
            CqpHttpApiResp resp = CqpHttpApi.getInstance().sendGroupMsg(GROUPID, info.toString());
            if (resp.getRetcode() == 0) {
                break;
            }
        }
    }
}
