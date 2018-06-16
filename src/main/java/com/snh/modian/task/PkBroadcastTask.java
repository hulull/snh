package com.snh.modian.task;

import com.snh.modian.domain.cqp.CqpHttpApiResp;
import com.snh.modian.domain.cqp.CqpRespGroupMsg;
import com.snh.modian.domain.modian.Detail;
import com.snh.modian.domain.modian.RankMoney;
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

//    @Scheduled(cron = "0 0 * * * ?")
    public void pkBroadcast() {
//        long time = System.currentTimeMillis();
//        if (time > TimeUtils.getZeroClock() && time < TimeUtils.getSevenClock()) {
//            return;
//        }
        List<Detail> detailList = ModianApi.queryDetails(UNIT_MODIANIDS);
        if (CollectionUtils.isEmpty(detailList)) {
            LOGGER.error("ModianApi.queryDetails({}) failed!", UNIT_MODIANIDS);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder("[世界杯小组赛]尼日利亚VS沙特阿拉伯——当前战况:\n");
        Collections.sort(detailList);
        // 设置总金额
        stringBuilder.append("总金额:\n");
        String format = "\t\t\t\t";
        for (Detail detail : detailList) {
            String name = detail.getPro_name().contains("姜杉") ? "姜杉" : "左婧媛";
            stringBuilder.append(format).append(name).append(":").append(detail.getAlready_raised()).append("\n");
        }
        // 第一名
        Detail detail1 = detailList.get(0);
        Detail detail2 = detailList.get(1);
        List<RankMoney> rankMoneyList1 = ModianApi.queryRankMoneys(Integer.valueOf(detail1.getPro_id()), 1);
        List<RankMoney> rankMoneyList2 = ModianApi.queryRankMoneys(Integer.valueOf(detail2.getPro_id()), 1);
        stringBuilder.append("第1名金额:\n");
        if (CollectionUtils.isEmpty(rankMoneyList1)) {
            stringBuilder.append(format).append("无");
        } else {
            stringBuilder.append(format).append(rankMoneyList1.get(0).getNickname()).append(":")
                    .append(rankMoneyList1.get(0).getBacker_money()).append("\n");
        }
        if (CollectionUtils.isEmpty(rankMoneyList2)) {
            stringBuilder.append(format).append("无");
        } else {
            stringBuilder.append(format).append(rankMoneyList2.get(0).getNickname()).append(":")
                    .append(rankMoneyList2.get(0).getBacker_money()).append("\n");
        }
        stringBuilder.append("第10名金额:\n");
        if (rankMoneyList1 != null && rankMoneyList1.size() >= 10) {
            stringBuilder.append(format).append(rankMoneyList1.get(9).getNickname()).append(":")
                    .append(rankMoneyList1.get(9).getBacker_money()).append("\n");
        } else {
            stringBuilder.append(format).append("无\n");
        }
        if (rankMoneyList2 != null && rankMoneyList1.size() >= 10) {
            stringBuilder.append(format).append(rankMoneyList2.get(9).getNickname()).append(":")
                    .append(rankMoneyList2.get(9).getBacker_money()).append("\n");
        } else {
            stringBuilder.append(format).append("无\n");
        }
        for (int i = 0; i < 5; i++) {
            CqpHttpApiResp resp = CqpHttpApi.getInstance().sendGroupMsg(GROUPID, stringBuilder.toString());
            if (resp.getRetcode() == 0) {
                break;
            }
        }
    }
}
