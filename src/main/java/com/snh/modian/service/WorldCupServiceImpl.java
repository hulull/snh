package com.snh.modian.service;

import com.snh.modian.constant.CardConstant;
import com.snh.modian.constant.RedisKey;
import com.snh.modian.constant.RedisOperation;
import com.snh.modian.domain.modian.Order;
import com.snh.modian.util.KeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class WorldCupServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(WorldCupServiceImpl.class);

    @Autowired
    RedisOperation<String> stringRedisOperation;
    @Value("${GROUPID}")
    private int GROUPID;
    @Value("${SR_BOUND}")
    private double SR_BOUND;

    private static final double CONSTANT = 10;

    public String getCardMsgWorldCup(Order order) {
        StringBuilder cardMsg = new StringBuilder();
        int rollTimes = (int) (order.getBacker_money() / CONSTANT);
        if (rollTimes == 0) {
            cardMsg.append("支持10元就可以获得抽卡机会，快来试试吧！\n");
        } else {
            cardMsg.append("获得了").append(rollTimes).append("次抽卡机会, 抽取以下卡片:\n");
            String showCard = null;
            String taskCard = null;
            Map<String, Integer> cardMap = new HashMap<>();
            if (taskCard == null && order.getBacker_money() >= 35) {
                taskCard = rollCard(order, 2);
            }
            for (int i = 0; i < rollTimes; i++) {
                int rank = rollRank(order);
                String card = rollCard(order, rank);
                showCard = card;
                if (rank == 1) {
                    if (cardMap.containsKey(card)) {
                        int value = cardMap.get(card) + 1;
                        cardMap.put(card, value);
                    } else {
                        cardMap.put(card, 1);
                    }
                } else {
                    taskCard = card;
                }
            }
            for (Map.Entry<String, Integer> entry : cardMap.entrySet()) {
                cardMsg.append(entry.getKey()).append("*").append(entry.getValue()).append("\n");
            }
            String file = "level1" + "/" + showCard + ".jpg";
            cardMsg.append("[CQ:image,file=").append(file).append("]\n");
            if (taskCard != null) {
                cardMsg.append("掉落惊喜:").append(taskCard).append("\n");
            }
            String file2 = "level2" + "/" + taskCard + ".jpg";
            cardMsg.append("[CQ:image,file=").append(file2).append("]\n");
        }
        return cardMsg.toString();
    }

    public int rollRank(Order order) {
        if (stringRedisOperation.hasKey(String.valueOf(order.getUser_id()))) {
            return 1;
        }
        double rollNum = Math.random();
        if (rollNum < SR_BOUND) {
            return 1;
        } else {
            return 2;
        }
    }

    public String rollCard(Order order, int rank) {
        String card = null;
        switch (rank) {
            case 1: {
                card = CardConstant.oridinary.get((int) (Math.random()*CardConstant.oridinary.size()));
                stringRedisOperation.incrementHash(KeyUtil.generateKey(RedisKey.USER_WORLD_CUP.getKey(), order.getNickname()), card);
                return card;
            }
            case 2: {
                card = CardConstant.special.get((int) (Math.random()*CardConstant.special.size()));
                stringRedisOperation.incrementHash(KeyUtil.generateKey(RedisKey.USER_TASK.getKey(), order.getNickname()), card);
                // 设置标志位
                stringRedisOperation.setCacheObject(String.valueOf(order.getUser_id()), "1");
                return card;
            }
            default: {
                card = CardConstant.oridinary.get((int) (Math.random()*CardConstant.oridinary.size()));
                stringRedisOperation.incrementHash(KeyUtil.generateKey(RedisKey.USER_WORLD_CUP.getKey(), order.getNickname()), card);
                return card;
            }
        }
    }
}
