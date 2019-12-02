package com.appzone.freshcrops.models;

import java.io.Serializable;

public class OrderStatusModel implements Serializable {
    int order_status;

    public OrderStatusModel(int order_status) {
        this.order_status = order_status;
    }

    public int getOrder_status() {
        return order_status;
    }
}
