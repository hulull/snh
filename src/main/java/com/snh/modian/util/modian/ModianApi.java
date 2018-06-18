package com.snh.modian.util.modian;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snh.modian.domain.modian.*;
import com.snh.modian.util.ConvertUtils;
import com.snh.modian.util.HttpUtil;
import com.snh.modian.util.SignDemo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModianApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModianApi.class);

    private static final String ORDER_URL = "https://wds.modian.com/api/project/orders";
    private static final String RANKING_URL = "https://wds.modian.com/api/project/rankings";
    private static final String RANK_URL = "https://wds.modian.com/api/project/rankings";
    private static final String DETAIL_URL = "https://wds.modian.com/api/project/detail";
    private static final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);


    /**
     * 1、项目订单查询
     * @param proId
     * @param page
     */
    public static List<Order> queryOrders(int proId, int page) {
        HashMap<String, String> params = new HashMap<String, String>(){{put("pro_id", String.valueOf(proId));
            put("page", String.valueOf(page));}};
        String sign = SignDemo.getSign(ORDER_URL, params);
        params.put("sign", sign);
        try {
            String result = HttpUtil.post(ORDER_URL, params);
            if (result != null) {
                JsonResult jsonResult = objectMapper.readValue(result, JsonResult.class);
                if (jsonResult.getStatus() == 0) {
                    List<Order> orderList = ConvertUtils.convertToList(jsonResult.getData(), objectMapper, Order.class);
                    return orderList;
                } else if (jsonResult.getStatus() == 2) {
                    LOGGER.warn("queryOrders return status is 2, url:{}, proId:{}, page:{}, msg:{}", ORDER_URL, proId, page,
                            jsonResult.getMessage());
                }
            }
        } catch (Exception e) {
            LOGGER.error(String.format("queryOrders(%d, %d) failed!", proId, page), e);
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * 2、聚聚榜
     * @param proId
     * @param page
     * @return
     */
    public static List<RankMoney> queryRankMoneys(int proId, int page) {
        HashMap<String, String> params = new HashMap<String, String>(){{put("pro_id", String.valueOf(proId));
            put("page", String.valueOf(page));
            put("type", "1");}};
        String sign = SignDemo.getSign(RANKING_URL, params);
        params.put("sign", sign);
        try {
            String result = HttpUtil.post(RANKING_URL, params);
            if (result != null) {
                JsonResult jsonResult = objectMapper.readValue(result, JsonResult.class);
                if (jsonResult.getStatus() == 0) {
                    List<RankMoney> rankMoneyList = ConvertUtils.convertToList(jsonResult.getData(), objectMapper, RankMoney.class);
                    return rankMoneyList;
                } else if (jsonResult.getStatus() == 2) {
                    LOGGER.warn("queryRankMoneys return status is 2, url:{}, proId:{}, page:{}, msg:{}", RANKING_URL, proId, page,
                            jsonResult.getMessage());
                }
            }
        } catch (Exception e) {
            LOGGER.error(String.format("queryRankMoneys(%d, %d) failed!", proId, page), e);
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * 3、打卡榜
     * @param proId
     * @param page
     * @return
     */
    public static List<RankDay> queryRankDays(int proId, int page) {
        HashMap<String, String> params = new HashMap<String, String>(){{put("pro_id", String.valueOf(proId));
            put("page", String.valueOf(page));
            put("type", "2");}};
        String sign = SignDemo.getSign(RANK_URL, params);
        params.put("sign", sign);
        try {
            String result = HttpUtil.post(RANK_URL, params);
            if (result != null) {
                JsonResult jsonResult = objectMapper.readValue(result, JsonResult.class);
                if (jsonResult.getStatus() == 0) {
                    List<RankDay> rankDayList = ConvertUtils.convertToList(jsonResult.getData(), objectMapper, RankDay.class);
                    return rankDayList;
                } else if (jsonResult.getStatus() == 2) {
                    LOGGER.warn("queryRankDays return status is 2, url:{}, proId:{}, page:{}, msg:{}", RANKING_URL, proId, page,
                            jsonResult.getMessage());
                }
            }
        } catch (Exception e) {
            LOGGER.error(String.format("queryRankDays(%d, %d) failed!", proId, page), e);
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * 4、项目进度
     * @param proId
     * @return
     */
    public static Detail queryDetail(int proId) {
        HashMap<String, String> params = new HashMap<String, String>(){{put("pro_id", String.valueOf(proId));}};
        String sign = SignDemo.getSign(DETAIL_URL, params);
        params.put("sign", sign);
        try {
            String result = HttpUtil.post(DETAIL_URL, params);
//            System.out.println(objectMapper.readTree(result).asText());
            if (result != null) {
                JsonResult jsonResult = objectMapper.readValue(result, JsonResult.class);
                if (jsonResult.getStatus() == 0) {
                    List<Detail> detailList = ConvertUtils.convertToList(jsonResult.getData(), objectMapper, Detail.class);
                    return detailList.get(0);
                } else if (jsonResult.getStatus() == 2) {
                    LOGGER.warn("queryDetail return status is 2, url:{}, proId:{}, msg:{}", DETAIL_URL, proId,
                            jsonResult.getMessage());
                }
            }
        } catch (Exception e) {
            LOGGER.error(String.format("queryDetail(%d) failed!", proId), e);
        }
        return null;
    }

    public static List<Detail> queryDetails(String proIds) {
        HashMap<String, String> params = new HashMap<String, String>(){{put("pro_id", proIds);}};
        StringBuilder signParam = new StringBuilder();
        String[] ids = proIds.split(",");
        for(String id : ids) {
            signParam.append(id).append("%2C");
        }
        HashMap<String, String> tmp = new HashMap<>();
        tmp.put("pro_id", signParam.substring(0, signParam.length()-3));
        String sign = SignDemo.getSign(DETAIL_URL, tmp);
        params.put("sign", sign);
        try {
            String result = HttpUtil.post(DETAIL_URL, params);
            JsonResult jsonResult = objectMapper.readValue(result, JsonResult.class);
            if (jsonResult.getStatus() == 0) {
                List<Detail> detailList = ConvertUtils.convertToList(jsonResult.getData(), objectMapper, Detail.class);
                return detailList;
            } else if (jsonResult.getStatus() == 2) {
                LOGGER.warn("queryDetail return status is 2, url:{}, proIds:{}, msg:{}", DETAIL_URL, proIds,
                        jsonResult.getMessage());
            }
        } catch (Exception e) {
            LOGGER.error(String.format("queryDetails(%s) failed!", proIds), e);
        }
        return Collections.EMPTY_LIST;
    }
}
