package com.snh.modian.domain.modian;

import java.util.Comparator;
import java.util.Date;

/**
 * 项目详情类
 */
public class Detail implements Comparable<Detail> {
    private String pro_id;
    private String pro_name;
    private String goal;
    private double already_raised;
    private long backer_count;
    private long success_order_count;
    private String end_time;
    private String pc_cover;
    private String mobile_cover;

    public Detail() {
    }

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public double getAlready_raised() {
        return already_raised;
    }

    public void setAlready_raised(double already_raised) {
        this.already_raised = already_raised;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getPc_cover() {
        return pc_cover;
    }

    public void setPc_cover(String pc_cover) {
        this.pc_cover = pc_cover;
    }

    public String getMobile_cover() {
        return mobile_cover;
    }

    public void setMobile_cover(String mobile_cover) {
        this.mobile_cover = mobile_cover;
    }

    public long getBacker_count() {
        return backer_count;
    }

    public void setBacker_count(long backer_count) {
        this.backer_count = backer_count;
    }

    public long getSuccess_order_count() {
        return success_order_count;
    }

    public void setSuccess_order_count(long success_order_count) {
        this.success_order_count = success_order_count;
    }

    @Override
    public String toString() {
        return "Detail{" +
                "pro_id='" + pro_id + '\'' +
                ", pro_name='" + pro_name + '\'' +
                ", goal='" + goal + '\'' +
                ", already_raised=" + already_raised +
                ", end_time='" + end_time + '\'' +
                ", pc_cover='" + pc_cover + '\'' +
                ", mobile_cover='" + mobile_cover + '\'' +
                '}';
    }

    @Override
    public int compareTo(Detail o) {
        return (int) Math.floor(o.getAlready_raised() - this.getAlready_raised());
    }
}
