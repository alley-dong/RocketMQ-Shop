package com.mashibing.model;
//优惠券去重表实体类
public class shopCouponUnique {
    private Long orderId;

    public shopCouponUnique(Long orderId) {
        this.orderId = orderId;
    }

    public shopCouponUnique() {
        super();
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}