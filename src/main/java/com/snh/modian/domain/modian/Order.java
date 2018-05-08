package com.snh.modian.domain.modian;

import java.util.Date;

/**
 * 订单类
 */
public class Order {
    private long user_id;
    private String nickname;
    private String pay_time;
    private double backer_money;

    public Order() {
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public double getBacker_money() {
        return backer_money;
    }

    public void setBacker_money(double backer_money) {
        this.backer_money = backer_money;
    }

    @Override
    public String toString() {
        return "Order{" +
                "user_id=" + user_id +
                ", nickname='" + nickname + '\'' +
                ", pay_time='" + pay_time + '\'' +
                ", backer_money=" + backer_money +
                '}';
    }
}
