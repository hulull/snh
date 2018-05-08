package com.snh.modian.util;

import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SignDemo {
    private static final String P = "das41aq6";
    public static String getSign(String baseUrl, Map<String, String> parameters) {
        SortUtils<String> sortUtils;
        List<String> keySet = new ArrayList<>();
        keySet.addAll(parameters.keySet());
        sortUtils = new SortUtils<String>();
        sortUtils.sort(keySet, "toString", null);
        String value = "";
        StringBuffer stringBuffer = new StringBuffer();
        if (keySet.size() > 0) {
            for (int i = 0; i < keySet.size(); i++) {
                if (i != 0) {
                    stringBuffer.append("&");
                }
                value = parameters.get(keySet.get(i));
                if (null == value || "null".equalsIgnoreCase(value)) {
                    value = "";
                }
                if (!StringUtils.isEmpty(value.trim())) {
                    stringBuffer.append(keySet.get(i));
                    stringBuffer.append("=");
                    stringBuffer.append(parameters.get(keySet.get(i)));
                }
            }
        }
        return encrypt(baseUrl, stringBuffer.toString());
    }
    public static String encrypt(String url, String params) {
        String str = params + "&p=" + P;
        str = MD5(str);
        return str.substring(5, 21);
    }
    public static String MD5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }
}