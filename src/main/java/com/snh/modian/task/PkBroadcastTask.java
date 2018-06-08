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

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

@Component
public class PkBroadcastTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(PkBroadcastTask.class);

    @Value("${UNIT_MODIANIDS}")
    private String UNIT_MODIANIDS;
    @Value("${GROUPID}")
    private int GROUPID;
    @Value("${GROUP1}")
    private String GROUP1;
    @Value("${GROUP2}")
    private String GROUP2;
    DecimalFormat df = new DecimalFormat("0.00");
    private static final long TIME = 1000 * 60 * 10;

    @Scheduled(cron = "0 0 * * * ?")
    public void pkBroadcast() {
        long time = System.currentTimeMillis();
        if (time > TimeUtils.getZeroClock() && time < TimeUtils.getSevenClock()) {
            return;
        }
//        List<Detail> detailList = ModianApi.queryDetails(UNIT_MODIANIDS);
//        if (CollectionUtils.isEmpty(detailList)) {
//            LOGGER.error("ModianApi.queryDetails({}) failed!", UNIT_MODIANIDS);
//            return;
//        }
//        Collections.sort(detailList);
//        StringBuilder info = new StringBuilder("当前排名如下:\n====================\n");
//        for (int i = 0; i < detailList.size(); i++) {
//            Detail detail = detailList.get(i);
//            if (TimeUtils.StringToLong(detail.getEnd_time()) < System.currentTimeMillis() + TIME) {
//                return;
//            }
//            int ranking = i + 1;
//            info.append("【").append(ranking).append("】");
//            info.append(detail.getPro_name()).append("\n");
//            info.append("￥").append(detail.getAlready_raised()).append("/").append("￥").append(detail.getGoal()).append("\n");
////            info.append("https://zhongchou.modian.com/item/").append(detail.getPro_id()).append(".html\n");
//            info.append("====================\n");
//        }
        // PK
        List<Detail> detailListB = ModianApi.queryDetails(GROUP1);
        List<Detail> detailListG = ModianApi.queryDetails(GROUP2);
        StringBuilder info = new StringBuilder("当前PK情况如下:\n====================\n");
        // 分组处理
        double total1 = 0.0;
        double total2 = 0.0;
        String name1 = "B城";
        String name2 = "G港";
        info.append("B城:\n");
        for (Detail d : detailListB) {
            if (TimeUtils.StringToLong(d.getEnd_time()) < System.currentTimeMillis() + TIME) {
                return;
            }
            info.append(d.getPro_name()).append(":￥").append(d.getAlready_raised()).append("/￥").append(d.getGoal()).append("\n");
            total1 += d.getAlready_raised();
        }
        info.append("====================\n");
        info.append("G港:\n");
        for (Detail d : detailListG) {
            total2 += d.getAlready_raised();
            info.append(d.getPro_name()).append(":￥").append(d.getAlready_raised()).append("/￥").append(d.getGoal()).append("\n");
        }
        info.append("====================\n");
        if (total1 > total2) {
            double gap = total1 - total2;
            info.append(name1).append(":").append("￥").append(df.format(total1)).append(" / ").append(name2).append(":").append("￥").append(df.format(total2)).append("\n");
            info.append(name1).append("领先了￥").append(df.format(gap)).append("\n====================\n");
        } else {
            double gap = total2 - total1;
            info.append(name1).append(":").append("￥").append(df.format(total1)).append("/").append(name2).append(":").append("￥").append(df.format(total2)).append("\n");
            info.append(name2).append("领先了￥").append(df.format(gap)).append("\n====================\n");
        }
        for (int i = 0; i <= 2; i++) {
            CqpHttpApiResp resp = CqpHttpApi.getInstance().sendGroupMsg(GROUPID, info.toString());
            if (resp.getRetcode() == 0) {
                break;
            }
        }
    }
}
