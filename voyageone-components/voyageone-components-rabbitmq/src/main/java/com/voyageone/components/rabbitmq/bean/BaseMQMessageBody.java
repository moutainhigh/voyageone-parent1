package com.voyageone.components.rabbitmq.bean;

import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;

/**
 * Created by dell on 2016/12/20.
 */
public abstract class BaseMQMessageBody implements IMQMessageBody {
    //消费者重试次数
    private int consumerRetryTimes = 0;

    //消息体id
    private int mqId;

    //延迟发送时间
    private int delaySecond;

    //发送者
    private String sender;

    public int getConsumerRetryTimes() {
        return consumerRetryTimes;
    }

    public void setConsumerRetryTimes(int consumerRetryTimes) {
        this.consumerRetryTimes = consumerRetryTimes;
    }

    public int getMqId() {
        return mqId;
    }

    public void setMqId(int mqId) {
        this.mqId = mqId;
    }

    @Override
    public int getDelaySecond() {
        return delaySecond;
    }

    public void setDelaySecond(int delaySecond) {
        this.delaySecond = delaySecond;
    }

    @Override
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public abstract void check() throws MQMessageRuleException;
}
