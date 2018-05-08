package com.snh.modian.domain.cqp;

public class CqpRespDiscussMsg extends CqpRespPrivMsg {

    private Boolean at_sender = true;

    public Boolean getAt_sender() {
        return at_sender;
    }

    public void setAt_sender(Boolean at_sender) {
        this.at_sender = at_sender;
    }
}
