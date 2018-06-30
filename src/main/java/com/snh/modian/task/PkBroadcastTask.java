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
import java.util.ArrayList;
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

    private List<RankMoney> getRankMoneyList(int proId) {
        List<RankMoney> rankMoneyList = new ArrayList<>();
        List<RankMoney> pageRankMoneyList;
        int page = 1;
        do {
            pageRankMoneyList = ModianApi.queryRankMoneys(proId, page);
            rankMoneyList.addAll(pageRankMoneyList);
            page++;
        } while (!CollectionUtils.isEmpty(pageRankMoneyList));
        return rankMoneyList;
    }

    private int getCountByMoney(List<RankMoney> rankMonies, double money) {
        int count = 0;
        for (RankMoney rankMoney : rankMonies) {
            if (Double.valueOf(rankMoney.getBacker_money()) >= money) {
                count++;
            }
        }
        return count;
    }

    public String getWorldCupInfo(List<Detail> detailList) {
        StringBuilder stringBuilder = new StringBuilder("[世界杯小组赛]尼日利亚vs阿根廷——当前战况:\n");
//        Collections.sort(detailList);
        // 设置总金额
        stringBuilder.append("总金额:\n");
        String format = "    ";
        Detail detail1 = detailList.get(0);
        Detail detail2 = detailList.get(1);
        double hxh_total = detail1.getAlready_raised();
        double other_total = detail2.getAlready_raised();
        double hxh_top1 = 0;
        double other_top1 = 0;
        double hxh_top10 = 0;
        double other_top10 = 0;
        int hxh_count = 0;
        int other_count = 0;
        int hxh_score = 0;
        int other_score = 0;
        for (Detail detail : detailList) {
            String name = detail.getPro_name().contains("胡晓慧") ? "胡晓慧" : "刘力菲";
            stringBuilder.append(format).append(name).append(":").append(detail.getAlready_raised()).append("\n");
        }
        // 第一名
        List<RankMoney> hxh_rank_list = getRankMoneyList(Integer.parseInt(detail1.getPro_id()));
        List<RankMoney> other_rank_list = getRankMoneyList(Integer.parseInt(detail2.getPro_id()));
        stringBuilder.append("第1名金额:\n");
        if (!CollectionUtils.isEmpty(hxh_rank_list)) {
            hxh_top1 = Double.parseDouble(hxh_rank_list.get(0).getBacker_money());
            stringBuilder.append(format).append(hxh_rank_list.get(0).getNickname()).append(":")
                    .append(hxh_rank_list.get(0).getBacker_money()).append("\n");
        }
        if (!CollectionUtils.isEmpty(other_rank_list)) {
            other_top1 = Double.parseDouble(other_rank_list.get(0).getBacker_money());
            stringBuilder.append(format).append(other_rank_list.get(0).getNickname()).append(":")
                    .append(other_rank_list.get(0).getBacker_money()).append("\n");
        }
        stringBuilder.append("第10名金额:\n");
        if (hxh_rank_list != null && hxh_rank_list.size() >= 10) {
            hxh_top10 = Double.parseDouble(hxh_rank_list.get(9).getBacker_money());
            stringBuilder.append(format).append(hxh_rank_list.get(9).getNickname()).append(":")
                    .append(hxh_rank_list.get(9).getBacker_money()).append("\n");
        }
        if (other_rank_list != null && other_rank_list.size() >= 10) {
            other_top10 = Double.parseDouble(other_rank_list.get(9).getBacker_money());
            stringBuilder.append(format).append(other_rank_list.get(9).getNickname()).append(":")
                    .append(other_rank_list.get(9).getBacker_money()).append("\n");
        }
        // 计算人数
        hxh_count = getCountByMoney(hxh_rank_list, 10);
        other_count = getCountByMoney(other_rank_list, 10);
        stringBuilder.append("10元以上人数:\n");
        stringBuilder.append(format).append(hxh_count).append("\n");
        stringBuilder.append(format).append(other_count).append("\n");
        // 计算得分
        if (hxh_total >= other_total || hxh_top1 >= other_top1 || hxh_top10 >= other_top10 || hxh_count >= other_count) {
            hxh_score++;
        }
        if (hxh_total <= other_total || hxh_top1 <= other_top1 || hxh_top10 <= other_top10 || hxh_count <= other_count) {
            other_score++;
        }
        return null;
    }

    public String getInfoPk(List<Detail> detailList) {
        StringBuilder stringBuilder = new StringBuilder("当前战况:\n");
        for (int i = 0; i < detailList.size(); i++) {
            Detail detail = detailList.get(i);
            stringBuilder.append(i+1).append(".").append(detail.getPro_name())
                    .append(" 支持人数:").append(detail.getBacker_count())
                    .append(" 已筹:").append("￥").append(detail.getAlready_raised()).append("\n");
        }
        return stringBuilder.toString();
    }

//    @Scheduled(cron = "0 0 * * * ?")
    public void pkBroadcast() {
        List<Detail> detailList = ModianApi.queryDetails(UNIT_MODIANIDS);
        if (CollectionUtils.isEmpty(detailList)) {
            LOGGER.error("ModianApi.queryDetails({}) failed!", UNIT_MODIANIDS);
            return;
        }
        Collections.sort(detailList);
        sendMsg(getInfoPk(detailList));
    }

    public void sendMsg(String info) {
        for (int i = 0; i < 5; i++) {
            CqpHttpApiResp resp = CqpHttpApi.getInstance().sendGroupMsg(GROUPID, info);
            if (resp.getRetcode() == 0) {
                break;
            }
        }
    }
}
