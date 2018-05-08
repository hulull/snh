package com.snh.modian.domain.modian;

/**
 * 聚聚打卡天数类
 */
public class RankDay {
    private String nickname;
    private int rank;
    private int support_days;

    public RankDay() {
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

    public int getSupport_days() {
        return support_days;
    }

    public void setSupport_days(int support_days) {
        this.support_days = support_days;
    }
}
