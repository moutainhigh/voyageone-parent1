package com.voyageone.service.bean.vms.channeladvisor.enums;

import com.voyageone.common.util.StringUtils;

import java.util.Arrays;

/**
 * @author aooer 2016/9/7.
 * @version 2.0.0
 * @since 2.0.0
 */
public enum OrderStatusEnum {

    Pending(1),
    ReleasedForShipment(2),
    AcknowledgedBySeller(3),
    PartiallyShipped(4),
    Shipped(5),
    Canceled(6);

    private int code;

    OrderStatusEnum(int code) {
        this.code = code;
    }


    public int getCode() {
        return this.code;
    }

    /**
     * 提供一个获取枚举的方法，能够根据数字获取枚举
     *
     * @param code code
     * @return 枚举
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public static OrderStatusEnum getInstance(int code) {
        return Arrays.stream(OrderStatusEnum.values()).filter(e -> e.code == code).findFirst().get();
    }

    /**
     * 提供一个获取枚举的方法，能够根据数字或者字符串获取枚举
     *
     * @param value value
     * @return 枚举
     */
    public static OrderStatusEnum getInstance(String value) {
        return StringUtils.isDigit(value) ? Arrays.asList(OrderStatusEnum.values()).stream().filter(e -> e.code == Integer.parseInt(value)).findFirst().get() : OrderStatusEnum.valueOf(value);
    }
}
