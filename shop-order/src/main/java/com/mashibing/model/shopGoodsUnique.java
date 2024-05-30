package com.mashibing.model;
//商品去重表表实体类
public class shopGoodsUnique {
    private Long orderId;

    public shopGoodsUnique(Long orderId) {
        this.orderId = orderId;
    }

    public shopGoodsUnique() {
        super();
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}