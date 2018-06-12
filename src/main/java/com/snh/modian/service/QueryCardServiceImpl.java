package com.snh.modian.service;

import com.snh.modian.constant.CardConstant;
import com.snh.modian.constant.RedisKey;
import com.snh.modian.constant.RedisOperation;
import com.snh.modian.domain.cqp.CqpHttpApiResp;
import com.snh.modian.util.KeyUtil;
import com.snh.modian.util.cqp.CqpHttpApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class QueryCardServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryCardServiceImpl.class);

    @Autowired
    private RedisOperation<String> stringRedisOperation;
    @Value("${GROUPID}")
    private int GROUPID;

    private static final List<Integer> cardSize;
    private DecimalFormat df = new DecimalFormat("0.00%");

    static {
        cardSize = new ArrayList<>();
        cardSize.add(CardConstant.pCard.size());
        cardSize.add(CardConstant.nCard.size());
        cardSize.add(CardConstant.rCard.size());
        cardSize.add(CardConstant.srCard.size());
        cardSize.add(CardConstant.ssrCard.size());
    }

    public void sendCardMsgByUsername(String username, long qq) {
        StringBuilder info = new StringBuilder("[CQ:at,qq=").append(qq).append("] ");
        info.append("{").append(username).append("}").append("查询结果为:");
        List<String> collectionRate = new ArrayList<>();
        RedisKey[] redisKeys = RedisKey.values();
        int emptyCount = 0;
        for (int i = 0; i < redisKeys.length; i++) {
            String key = KeyUtil.generateKey(redisKeys[i].getKey(), username);
            Map<String, String> map = stringRedisOperation.getAllHashValue(key);
            if (CollectionUtils.isEmpty(map)) {
                emptyCount++;
                if (emptyCount == 5) {
                    info.append("您的查询结果为空,请输入正确的摩点ID.");
                    sendMsg(info.toString());
                    return;
                }
                continue;
            }
            info.append("\n====================\n");
            for (Map.Entry<String, String> entry : map.entrySet()) {
                info.append("【").append(entry.getKey()).append("×").append(entry.getValue()).append("】");
            }
            double rate = map.size()*1.0 / cardSize.get(i);
            collectionRate.add(df.format(rate));
        }
        info.append("\n====================\n当前卡片收集率:\n");
        for (int i = 0; i < collectionRate.size(); i++) {
            String rate = collectionRate.get(i);
            if (rate.equalsIgnoreCase("0.00%")) {
                continue;
            }
            info.append(i+1).append("星:").append(collectionRate.get(i)).append("\n");
        }
        LOGGER.info(info.toString());
        sendMsg(info.toString());
//        System.out.println(info);
    }

    private void sendMsg(String info) {
        for (int i = 0; i < 5; i++) {
            CqpHttpApiResp resp = CqpHttpApi.getInstance().sendGroupMsg(GROUPID, info.toString());
            if (resp.getRetcode() == 0) {
                break;
            }
        }
    }
}
