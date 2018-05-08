package com.snh.modian.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

    private static PoolingHttpClientConnectionManager clientConnectionManager = null;
    private static volatile CloseableHttpClient httpClient = null;
    private static RequestConfig config = RequestConfig.custom()
            .setConnectionRequestTimeout(3000)
            .setConnectTimeout(3000)
            .setSocketTimeout(3000)
            .build();

    static {
        clientConnectionManager = new PoolingHttpClientConnectionManager();
        clientConnectionManager.setMaxTotal(100);
        clientConnectionManager.setDefaultMaxPerRoute(clientConnectionManager.getMaxTotal());
    }

    private static CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            synchronized (HttpUtil.class) {
                if (httpClient == null) {
                    httpClient = HttpClients.custom().setConnectionManager(clientConnectionManager).build();
                }
            }
        }
        return httpClient;
    }

    public static String modianPost(String url, Map<String, String> params) {
        String sign = SignDemo.getSign(url, params);
        params.put("sign", sign);
        HttpPost httPost = new HttpPost(url);
        httPost.setConfig(config);
        httPost.setHeader("User-Agent", "Modian");
        List<NameValuePair> formatParams = new ArrayList<NameValuePair>();
        if (!CollectionUtils.isEmpty(params)) {
            params.forEach((key, value)->{
                formatParams.add(new BasicNameValuePair(key, value));
            });
        }
        httPost.setEntity(new UrlEncodedFormEntity(formatParams, StandardCharsets.UTF_8));
        try {
            HttpResponse response = getHttpClient().execute(httPost);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity); // 这里返回的是Unicode编码
        } catch (Exception e) {
            LOGGER.error(String.format("post(%s, %s) failed!", url, params.toString()), e);
            return null;
        }
    }

    public static String post(String url, Map<String, String> params){
        HttpPost httPost = new HttpPost(url);
        httPost.setConfig(config);
        List<NameValuePair> formatParams = new ArrayList<NameValuePair>();
        if (!CollectionUtils.isEmpty(params)) {
            params.forEach((key, value)->{
                formatParams.add(new BasicNameValuePair(key, value));
            });
        }
        httPost.setEntity(new UrlEncodedFormEntity(formatParams, StandardCharsets.UTF_8));
        try {
            HttpResponse response = getHttpClient().execute(httPost);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity); // 这里返回的是Unicode编码
        } catch (Exception e) {
            LOGGER.error(String.format("post(%s, %s) failed!", url, params.toString()), e);
            return null;
        }
    }

    public static String get(String url){
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(config);
        try {
            String result = getHttpClient().execute(httpGet, (httpResponse) -> {
                int responseCode = httpResponse.getStatusLine().getStatusCode();
                if (responseCode >= 200 && responseCode < 300) {
                    HttpEntity entity = httpResponse.getEntity();
                    return EntityUtils.toString(entity, "utf-8");
                } else {
                    LOGGER.error("error code " + responseCode);
                }
                return null;
            });
            return result;
        } catch (IOException e) {
            LOGGER.error(e.toString());
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
        return null;
    }
}
