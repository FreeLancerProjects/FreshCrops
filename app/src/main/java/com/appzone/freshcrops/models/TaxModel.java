package com.appzone.freshcrops.models;

import java.io.Serializable;

public class TaxModel implements Serializable {
    private int product_tax;
    private double minimum_client_order_price;

    public int getProduct_tax() {
        return product_tax;
    }

    public double getMinimum_client_order_price() {
        return minimum_client_order_price;
    }
}
