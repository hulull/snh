package com.snh.modian.domain.modian;

/**
 * 聚聚打卡金额类
 */
public class RankMoney {
    private long user_id;
    private String nickname;
    private int rank;
    private String backer_money;

    public RankMoney() {
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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getBacker_money() {
        return backer_money;
    }

    public void setBacker_money(String backer_money) {
        this.backer_money = backer_money;
    }
}
