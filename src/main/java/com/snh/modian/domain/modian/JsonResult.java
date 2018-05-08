package com.snh.modian.domain.modian;

import java.io.Serializable;

public class JsonResult implements Serializable{
    private static final long serialVersionUID = -1L;
    private int status;
    private String message;
    private Object data;
    private long mapi_query_time;

    public JsonResult() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public long getMapi_query_time() {
        return mapi_query_time;
    }

    public void setMapi_query_time(long mapi_query_time) {
        this.mapi_query_time = mapi_query_time;
    }
}
