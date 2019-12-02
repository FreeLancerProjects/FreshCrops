package com.appzone.freshcrops.models;

import java.io.Serializable;
import java.util.List;

public class DelegateCollectingOrderUploadModel implements Serializable {

    private int order_id;
    private double total_order_cost;
    private String token;
    private List<Products> products;

    public DelegateCollectingOrderUploadModel() {
    }

    public DelegateCollectingOrderUploadModel(int order_id, double total_order_cost, String token, List<Products> productsList) {
        this.order_id = order_id;
        this.total_order_cost = total_order_cost;
        this.token = token;
        this.products = productsList;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public double getTotal_order_cost() {
        return total_order_cost;
    }

    public void setTotal_order_cost(double total_order_cost) {
        this.total_order_cost = total_order_cost;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Products> getProductsList() {
        return products;
    }

    public void setProductsList(List<Products> productsList) {
        this.products = productsList;
    }


}
