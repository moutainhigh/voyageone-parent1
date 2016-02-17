package com.voyageone.cms.feed;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 枚举 Operation 的 Bean 定义
 * @author Jonas 9/2/15.
 * @version 2.0.0
 * @since 1.0.0
 */
public class OperationBean {

    private final Operation operation;

    protected OperationBean(Operation operation) {
        this.operation = operation;
    }

    public String getDesc() {
        return getOperation().desc();
    }

    public boolean isSingle() {
        return getOperation().isSingle();
    }

    public String getName() {
        return getOperation().name();
    }

    @JsonIgnore
    public Operation getOperation() {
        return operation;
    }
}
