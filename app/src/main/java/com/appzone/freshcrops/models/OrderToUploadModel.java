package com.appzone.freshcrops.models;

import java.io.Serializable;
import java.util.List;

public class OrderToUploadModel implements Serializable {

    private int client_id;
    private String client_name;
    private String client_phone;
    private String client_alternative_phone;
    private String client_address;
    private double lat;
    private double lng;
    private String client_street;
    private String notes;
    private int time_type;
    private String coupon_code;
    private double coupon_value;
    private String payment_method;
    private int discount_by_use;
    private double tax;
    private double total_discount;
    private double order_total_price_net;
    private double order_total_price;
    private double delivery_cost;
    private double discount_point;
    private List<OrderItem> orderItemList;

    public OrderToUploadModel() {
    }


    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_Phone() {
        return client_phone;
    }

    public void setClient_Phone(String client_Phone) {
        this.client_phone = client_Phone;
    }

    public String getClient_alternative_phone() {
        return client_alternative_phone;
    }

    public void setClient_alternative_phone(String client_alternative_phone) {
        this.client_alternative_phone = client_alternative_phone;
    }

    public String getClient_address() {
        return client_address;
    }

    public void setClient_address(String client_address) {
        this.client_address = client_address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getClient_street() {
        return client_street;
    }

    public void setClient_street(String client_street) {
        this.client_street = client_street;
    }

    public double getTotal_discount() {
        return total_discount;
    }

    public void setTotal_discount(double total_discount) {
        this.total_discount = total_discount;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getOrder_total_price_net() {
        return order_total_price_net;
    }

    public void setOrder_total_price_net(double order_total_price_net) {
        this.order_total_price_net = order_total_price_net;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getTime_type() {
        return time_type;
    }

    public void setTime_type(int time_type) {
        this.time_type = time_type;
    }

    public double getCoupon_value() {
        return coupon_value;
    }

    public void setCoupon_value(double coupon_value) {
        this.coupon_value = coupon_value;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public double getOrder_total_price() {
        return order_total_price;
    }

    public void setOrder_total_price(double order_total_price) {
        this.order_total_price = order_total_price;
    }

    public void setDiscount_by_use(int discount_by_use) {
        this.discount_by_use = discount_by_use;
    }

    public int getDiscount_by_use() {
        return discount_by_use;
    }

    public String getClient_phone() {
        return client_phone;
    }

    public void setClient_phone(String client_phone) {
        this.client_phone = client_phone;
    }

    public double getDelivery_cost() {
        return delivery_cost;
    }

    public void setDelivery_cost(double delivery_cost) {
        this.delivery_cost = delivery_cost;
    }

    public double getDiscount_point() {
        return discount_point;
    }

    public void setDiscount_point(double discount_point) {
        this.discount_point = discount_point;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
