package com.snh.modian.domain.cqp;

public class CqpRespPrivMsg {

    private Boolean block = true;

    private String reply;

    private Boolean auto_escape = false;

    public Boolean getBlock() {
        return block;
    }

    public void setBlock(Boolean block) {
        this.block = block;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public Boolean getAuto_escape() {
        return auto_escape;
    }

    public void setAuto_escape(Boolean auto_escape) {
        this.auto_escape = auto_escape;
    }

}
