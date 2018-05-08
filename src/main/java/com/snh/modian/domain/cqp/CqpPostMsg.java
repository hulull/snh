package com.snh.modian.domain.cqp;

public class CqpPostMsg {

    /**
     * 请求类型：
     * message	收到消息
     * event	群、讨论组变动等非消息类事件
     * request	加好友请求、加群请求／邀请
     */
    private String post_type;

    /**
     * 消息类型：
     * private 私聊消息
     * discuss 讨论组消息
     * group 群消息
     */
    private String message_type;

    /**
     * 私聊消息："friend"、"group"、"discuss"、"other"
     * 群消息："normal"、"anonymous"、"notice"
     */
    private String sub_type;

    /**
     * 消息ID
     */
    private int message_id;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 来源QQ
     */
    private long user_id;

    /**
     * 字体
     */
    private long font;

    /**
     * 群号
     */
    private long group_id;

    /**
     * 匿名用户名
     */
    private String anonymous;

    /**
     * 匿名用户 flag，在调用禁言 API 时需要传入
     */
    private String anonymous_flag;

    /**
     * 讨论组ID
     */
    private long discuss_id;

    /**
     * 文件信息
     */
    private Object file;

    private String event;

    private long operator_id;

    private String request_type;

    private String flag;

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getSub_type() {
        return sub_type;
    }

    public void setSub_type(String sub_type) {
        this.sub_type = sub_type;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getFont() {
        return font;
    }

    public void setFont(long font) {
        this.font = font;
    }

    public long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(long group_id) {
        this.group_id = group_id;
    }

    public String getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(String anonymous) {
        this.anonymous = anonymous;
    }

    public String getAnonymous_flag() {
        return anonymous_flag;
    }

    public void setAnonymous_flag(String anonymous_flag) {
        this.anonymous_flag = anonymous_flag;
    }

    public long getDiscuss_id() {
        return discuss_id;
    }

    public void setDiscuss_id(long discuss_id) {
        this.discuss_id = discuss_id;
    }

    public Object getFile() {
        return file;
    }

    public void setFile(Object file) {
        this.file = file;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public long getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(long operator_id) {
        this.operator_id = operator_id;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public CqpPostMsg() {
    }
}
