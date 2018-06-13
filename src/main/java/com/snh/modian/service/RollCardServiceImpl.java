package com.snh.modian.service;

import com.snh.modian.constant.CardConstant;
import com.snh.modian.constant.ModianConstant;
import com.snh.modian.constant.RedisKey;
import com.snh.modian.constant.RedisOperation;
import com.snh.modian.domain.cqp.CqpHttpApiResp;
import com.snh.modian.domain.modian.Detail;
import com.snh.modian.domain.modian.Order;
import com.snh.modian.domain.modian.RankMoney;
import com.snh.modian.util.KeyUtil;
import com.snh.modian.util.cqp.CqpHttpApi;
import com.snh.modian.util.modian.ModianApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.util.*;

@Service
public class RollCardServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(RollCardServiceImpl.class);

    @Autowired
    RedisOperation<String> stringRedisOperation;
    @Value("${GROUPID}")
    private int GROUPID;
    @Value("${P_BOUND}")
    private double P_BOUND;
    @Value("${N_BOUND}")
    private double N_BOUND;
    @Value("${R_BOUND}")
    private double R_BOUND;
    @Value("${SR_BOUND}")
    private double SR_BOUND;

    private static final double CONSTANT = 9.16;
    DecimalFormat df = new DecimalFormat("0.00");

    public int rollTimes(double money) {
        if (money == 10 * CONSTANT) {
            return 11;
        }
        if (money == 100 * CONSTANT) {
            return 111;
        }
        return (int) (money / CONSTANT);
    }

    public int rollRank() {
        double rollNum = Math.random();
        if (rollNum < P_BOUND) {
            return 1;
        } else if (rollNum < N_BOUND) {
            return 2;
        } else if (rollNum < R_BOUND) {
            return 3;
        } else if (rollNum < SR_BOUND) {
            return 4;
        } else {
            return 5;
        }
    }

    public String rollCardByRank(int rank, String username) {
        String card = null;
        switch (rank) {
            case 5:
                card = CardConstant.ssrCard.get((int) (Math.random()*CardConstant.ssrCard.size()));
                stringRedisOperation.incrementHash(KeyUtil.generateKey(RedisKey.USER_SSR.getKey(), username), card);
                return card;
            case 4:
                card = CardConstant.srCard.get((int) (Math.random()*CardConstant.srCard.size()));
                stringRedisOperation.incrementHash(KeyUtil.generateKey(RedisKey.USER_SR.getKey(), username), card);
                return card;
            case 3:
                card = CardConstant.rCard.get((int) (Math.random()*CardConstant.rCard.size()));
                stringRedisOperation.incrementHash(KeyUtil.generateKey(RedisKey.USER_R.getKey(), username), card);
                return card;
            case 2:
                card = CardConstant.nCard.get((int) (Math.random()*CardConstant.nCard.size()));
                stringRedisOperation.incrementHash(KeyUtil.generateKey(RedisKey.USER_N.getKey(), username), card);
                return card;
            case 1:
                card = CardConstant.pCard.get((int) (Math.random()*CardConstant.pCard.size()));
                stringRedisOperation.incrementHash(KeyUtil.generateKey(RedisKey.USER_P.getKey(), username), card);
                return card;
            default:
                card = CardConstant.pCard.get((int) (Math.random()*CardConstant.pCard.size()));
                stringRedisOperation.incrementHash(KeyUtil.generateKey(RedisKey.USER_P.getKey(), username), card);
                return card;
        }
    }

    public Map<String, String> getUserRankDetail(int modainId, String username) {
        Map<String, String> rankDetail = new HashMap<>();
        List<RankMoney> rankMoneyList = new ArrayList<>();
        int page = 1;
        RankMoney userRankMoney = null;
        RankMoney formerUserRankMoney = null;
        List<RankMoney> pageRankMoneyList = null;
        do {
            pageRankMoneyList = ModianApi.queryRankMoneys(modainId, page);
            rankMoneyList.addAll(pageRankMoneyList);
            page++;
        } while (!CollectionUtils.isEmpty(pageRankMoneyList));
        for (RankMoney rankMoney : rankMoneyList) {
            if (rankMoney.getNickname().equals(username)) {
                userRankMoney = rankMoney;
                rankDetail.put(ModianConstant.USER_RANK, String.valueOf(rankMoney.getRank()));
                rankDetail.put(ModianConstant.USER_RAISED_MONEY, rankMoney.getBacker_money());
                break;
            }
        }
        if (userRankMoney.getRank() != 1) {
            for (RankMoney rankMoney : rankMoneyList) {
                if (rankMoney.getRank() == userRankMoney.getRank() - 1) {
                    formerUserRankMoney = rankMoney;
                    break;
                }
            }
            double gap = Double.valueOf(formerUserRankMoney.getBacker_money()) - Double.valueOf(userRankMoney.getBacker_money());
            rankDetail.put(ModianConstant.GAP, df.format(gap));
        }
        return rankDetail;
    }

    public void rollCard(int modianId, List<Order> orderList) {
        for (Order order : orderList) {
            String info = null;
            // 和上一名差值
            Map<String, String> userRankDetail = getUserRankDetail(modianId, order.getNickname());
            if (userRankDetail.size() == 3) {
                info = order.getNickname() + " 爸爸刚刚支持了" + order.getBacker_money() + "元，在当前项目中已集资" + userRankDetail.get(ModianConstant.USER_RAISED_MONEY) +
                        "元，当前排名" + userRankDetail.get("userRank") + "，距离上一名还差" + userRankDetail.get(ModianConstant.GAP) + "元。\n====================\n";
            } else {
                info = order.getNickname() + " 爸爸刚刚支持了" + order.getBacker_money() + "元，在当前项目中已集资" + userRankDetail.get(ModianConstant.USER_RAISED_MONEY) +
                        "元，当前排名" + userRankDetail.get(ModianConstant.USER_RANK) + "\n====================\n";
            }
            info += getCardMsg(order); // 获取抽卡信息
            Detail detail = ModianApi.queryDetail(modianId);
//            System.out.println(detail.toString());
            double gap = Double.valueOf(detail.getGoal()) - detail.getAlready_raised();
            if (gap > 0) {
                info += "====================\n已筹" + detail.getAlready_raised() + "元，距离目标还差" + df.format(gap) + "元。\n====================\n";
            } else {
                info += "====================\n已筹" + detail.getAlready_raised() + "元。\n====================\n";
            }
            info += detail.getPro_name() + " 链接" + "https://zhongchou.modian.com/item/" + detail.getPro_id() + ".html";
            LOGGER.info(info);
            for (int i = 0; i < 5; i++) {
                CqpHttpApiResp resp = CqpHttpApi.getInstance().sendGroupMsg(GROUPID, info);
                if (resp.getRetcode() == 0) {
                    break;
                }
            }
        }
    }

    public String getCardMsg(Order order) {
        String cardMsg;
        int rollTimes = rollTimes(order.getBacker_money());
        if (rollTimes == 0) {
            cardMsg = "支持9.16元就可以获得抽卡机会，快来试试吧！\n";
        } else {
            cardMsg = "获得了" + rollTimes + "次抽卡机会, 抽取以下卡片:\n";
            Map<Integer, String> superCard = new HashMap<>(); // 卡片等级, key-card,value-level
            Map<String, Integer> rollCardMap = new HashMap<>(); // 抽卡记录, key-card,value-抽到个数
            int maxLevel = 0;
            if (order.getBacker_money() >= 10 * CONSTANT) {
                rollTimes--;
            }
            if (order.getBacker_money() >= 100 * CONSTANT) { // 保底5星
                maxLevel = 5;
                String card = rollCardByRank(maxLevel, order.getNickname());
                superCard.put(maxLevel, card);
                rollCardMap.put(card, 1);
            } else if (order.getBacker_money() >= 10 * CONSTANT) { // 保底4星
                maxLevel = 4;
                String card = rollCardByRank(maxLevel, order.getNickname());
                superCard.put(maxLevel, card);
                rollCardMap.put(card, 1);
            }
            for (int i = 1; i <= rollTimes; i++) {
                int rank = rollRank();
                maxLevel = Math.max(rank, maxLevel);
                String card = rollCardByRank(rank, order.getNickname());
                superCard.put(rank, card);
                if (rollCardMap.containsKey(card)) {
                    int value = rollCardMap.get(card) + 1;
                    rollCardMap.put(card, value);
                } else {
                    rollCardMap.put(card, 1);
                }
            }
            String returnCard = superCard.get(maxLevel);
            String file = "l" + maxLevel + "/" + returnCard + ".jpg";
            for (Map.Entry<String, Integer> entry : rollCardMap.entrySet()) {
                cardMsg += "【" + entry.getKey() + "×" + entry.getValue() + "】\n";
            }
            cardMsg += "[CQ:image,file=" + file + "]\n";
        }
        return cardMsg;
    }

    public void rollCardV2(int modianId, List<Order> orderList) {
        for (Order order : orderList) {
            String info = order.getNickname() + " 爸爸刚刚支持了" + order.getBacker_money() + "元。\n====================\n";
            info += getCardMsg(order);
            Detail detail = ModianApi.queryDetail(modianId);
//            System.out.println(detail.toString());
            double gap = Double.valueOf(detail.getGoal()) - detail.getAlready_raised();
            if (gap > 0) {
                info += "====================\n已筹" + detail.getAlready_raised() + "元，距离目标还差" + df.format(gap) + "元。\n====================\n";
            } else {
                info += "====================\n已筹" + detail.getAlready_raised() + "元。\n====================\n";
            }
            info += detail.getPro_name() + " 链接" + "https://zhongchou.modian.com/item/" + detail.getPro_id() + ".html";
            LOGGER.info(info);
            for (int i = 0; i < 5; i++) {
                CqpHttpApiResp resp = CqpHttpApi.getInstance().sendGroupMsg(GROUPID, info);
                if (resp.getRetcode() == 0) {
                    break;
                }
            }
        }
    }
}
