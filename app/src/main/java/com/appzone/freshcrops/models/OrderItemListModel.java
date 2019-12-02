package com.appzone.freshcrops.models;

import java.io.Serializable;
import java.util.List;

public class OrderItemListModel implements Serializable {

    private List<OrderItem> data;

    public List<OrderItem> getData() {
        return data;
    }
}
