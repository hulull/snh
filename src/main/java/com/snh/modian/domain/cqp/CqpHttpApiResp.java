package com.snh.modian.domain.cqp;

import java.util.Map;

public class CqpHttpApiResp {

    private int retcode;

    private String status;

    private Map<String, Object> data;

    public CqpHttpApiResp() {
    }

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
