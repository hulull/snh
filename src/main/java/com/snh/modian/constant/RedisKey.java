package com.snh.modian.constant;

public enum RedisKey {
    USER_P("user_p_"),
    USER_N("user_n_"),
    USER_R("user_r_"),
    USER_SR("user_sr_"),
    USER_SSR("user_ssr_");

    private String prefix;

    RedisKey(String prefix) {
        this.prefix = prefix;
    }

    public String getKey() {
        return prefix;
    }
}
