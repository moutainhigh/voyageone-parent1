package com.voyageone.service.model.cms.mongo.channeladvisor;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class CABaseModel {


    @JsonIgnore
    public boolean hasErrors() {
        return false;
    }

}
