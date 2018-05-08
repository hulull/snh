package com.snh.modian.task;

import com.snh.modian.constant.ModianConstant;
import com.snh.modian.constant.RedisKey;
import com.snh.modian.constant.RedisOperation;
import com.snh.modian.domain.modian.Detail;
import com.snh.modian.domain.modian.Order;
import com.snh.modian.domain.modian.RankMoney;
import com.snh.modian.util.KeyUtil;
import com.snh.modian.util.TimeUtils;
import com.snh.modian.util.cqp.CqpHttpApi;
import com.snh.modian.util.modian.ModianApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.util.*;

@Component
public class ModianTask implements Runnable {

    @Autowired
    RedisOperation<String> stringRedisOperation;
    @Value("${MODIANID}")
    private int MODIANID;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ModianTask.class);

    private static Long newestClockIn = -1L;
    private static final long TIME_INTERVAL = 5*1000;
    private static final double CONSTANT = 9.16;

    List<String> pCard = Arrays.asList("包 is watching you-1星", "恶犬-1星", "神秘冰淇淋-1星", "先睡一步-1星", "桌布-1星");
    List<String> nCard = Arrays.asList("暗中观察-2星", "嘟嘴思考-2星", "聆听-2星", "毛巾广告-2星", "歪头杀-2星", "稀有发型-2星");
    List<String> rCard = Arrays.asList("暗夜巡行-3星", "柴犬懵逼-3星", "粉红少女心-3星", "裹紧小毯子-3星", "红白巫女-3星", "花环-3星", "机场时尚-3星", "灵魂歌手-3星", "美杜莎-3星", "青涩容颜-3星", "犬式围笑-3星", "人偶-3星", "若有所思-3星", "上目线-3星", "吸引注意力的眉毛-3星", "夏日阳光-3星");
    List<String> srCard = Arrays.asList("仓鼠少女-4星","柴的凝视-4星", "冬日暖阳-4星", "放课后-4星", "帅气学姐-4星", "委屈包包-4星", "校园回忆-4星","勇往直前-4星","雨中漫步-4星");
    List<String> ssrCard = Arrays.asList("不羁行者-5星","测量-5星","吾辈是猫-5星");

//    @Scheduled(fixedDelay = TIME_INTERVAL)
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
            maxPayTime = Math.max(TimeUtils.StringToLong(order.getPay_time()), newestClockIn);
        }
        newestClockIn = maxPayTime;
        rollCard(resultList);
        return true;
    }

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
                card = ssrCard.get((int) (Math.random()*ssrCard.size()));
                stringRedisOperation.incrementHash(KeyUtil.generateKey(RedisKey.USER_SSR.getKey(), username), card);
                return card;
            case 4:
                card = srCard.get((int) (Math.random()*srCard.size()));
                stringRedisOperation.incrementHash(KeyUtil.generateKey(RedisKey.USER_SR.getKey(), username), card);
                return card;
            case 3:
                card = rCard.get((int) (Math.random()*rCard.size()));
                stringRedisOperation.incrementHash(KeyUtil.generateKey(RedisKey.USER_R.getKey(), username), card);
                return card;
            case 2:
                card = nCard.get((int) (Math.random()*nCard.size()));
                stringRedisOperation.incrementHash(KeyUtil.generateKey(RedisKey.USER_N.getKey(), username), card);
                return card;
            case 1:
                card = pCard.get((int) (Math.random()*pCard.size()));
                stringRedisOperation.incrementHash(KeyUtil.generateKey(RedisKey.USER_P.getKey(), username), card);
                return card;
            default:
                card = pCard.get((int) (Math.random()*pCard.size()));
                stringRedisOperation.incrementHash(KeyUtil.generateKey(RedisKey.USER_P.getKey(), username), card);
                return card;
        }
    }

    private Map<String, String> getUserRankDetail(String username) {
        Map<String, String> rankDetail = new HashMap<>();
        List<RankMoney> rankMoneyList = new ArrayList<>();
        int page = 1;
        RankMoney userRankMoney = null;
        RankMoney formerUserRankMoney = null;
        List<RankMoney> pageRankMoneyList = null;
        do {
            pageRankMoneyList = ModianApi.queryRankMoneys(MODIANID, page);
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
            DecimalFormat df = new DecimalFormat("0.00");
            rankDetail.put(ModianConstant.GAP, df.format(gap));
        }
        return rankDetail;
    }

    private void rollCard(List<Order> orderList) {
        for (Order order : orderList) {
            int rollTimes = rollTimes(order.getBacker_money());
            String info = null;
            // 和上一名差值
            Map<String, String> userRankDetail = getUserRankDetail(order.getNickname());
            if (userRankDetail.size() == 3) {
                info = order.getNickname() + " 爸爸刚刚支持了" + order.getBacker_money() + "元，在当前项目中已集资" + userRankDetail.get(ModianConstant.USER_RAISED_MONEY) +
                        "元，当前排名" + userRankDetail.get("userRank") + "，距离上一名还差" + userRankDetail.get(ModianConstant.GAP) + "元。\n====================\n";
            } else {
                info = order.getNickname() + " 爸爸刚刚支持了" + order.getBacker_money() + "元，在当前项目中已集资" + userRankDetail.get(ModianConstant.USER_RAISED_MONEY) +
                        "元，当前排名" + userRankDetail.get(ModianConstant.USER_RANK) + "\n====================\n";
            }
            if (rollTimes == 0) {
                info += "支持9.16元就可以获得抽卡机会，快来试试吧！\n";
            } else {
                info += "获得了" + rollTimes + "次抽卡机会, 抽取以下卡片:\n";
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
                String file = "level" + maxLevel + "/" + returnCard + ".png";
                for (Map.Entry<String, Integer> entry : rollCardMap.entrySet()) {
                    info += "【" + entry.getKey() + "×" + entry.getValue() + "】\n";
                }
                info += "[CQ:image,file=" + file + "]\n";
            }
            Detail detail = ModianApi.queryDetail(MODIANID);
//            System.out.println(detail.toString());
            double gap = Double.valueOf(detail.getGoal()) - detail.getAlready_raised();
            info += "====================\n已筹" + detail.getAlready_raised() + "元，距离目标还差" + gap + "元。\n====================\n"
                    + detail.getPro_name() + " 链接" + "https://zhongchou.modian.com/item/" + detail.getPro_id() + ".html";
            System.out.println(info);
//            CqpHttpApi.getInstance().sendGroupMsg(GROUPID, info);
        }
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
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
