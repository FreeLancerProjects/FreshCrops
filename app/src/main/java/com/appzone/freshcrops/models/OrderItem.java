package com.appzone.freshcrops.models;

import java.io.Serializable;

public class OrderItem implements Serializable
{
    private String product_image;
    private int product_id;
    private int feature_id;
    private int product_price_id;
    private String product_name_ar;
    private String product_name_en;
    private String product_size_ar;
    private String product_size_en;
    private int product_quantity;
    private double product_price;
    private double product_total_price;
    private AlternativeProductItem alternativeProductItem;

    public OrderItem() {
    }

    public OrderItem(String product_image, int product_id, int feature_id, int product_price_id, String product_name_ar, String product_name_en, String product_size_ar, String product_size_en, int product_quantity, double product_price, double product_total_price) {
        this.product_image = product_image;
        this.product_id = product_id;
        this.feature_id = feature_id;
        this.product_price_id = product_price_id;
        this.product_name_ar = product_name_ar;
        this.product_name_en = product_name_en;
        this.product_size_ar = product_size_ar;
        this.product_size_en = product_size_en;
        this.product_quantity = product_quantity;
        this.product_price = product_price;
        this.product_total_price = product_total_price;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getFeature_id() {
        return feature_id;
    }

    public void setFeature_id(int feature_id) {
        this.feature_id = feature_id;
    }

    public int getProduct_price_id() {
        return product_price_id;
    }

    public void setProduct_price_id(int product_price_id) {
        this.product_price_id = product_price_id;
    }

    public String getProduct_name_ar() {
        return product_name_ar;
    }

    public void setProduct_name_ar(String product_name_ar) {
        this.product_name_ar = product_name_ar;
    }

    public String getProduct_name_en() {
        return product_name_en;
    }

    public void setProduct_name_en(String product_name_en) {
        this.product_name_en = product_name_en;
    }

    public String getProduct_size_ar() {
        return product_size_ar;
    }

    public void setProduct_size_ar(String product_size_ar) {
        this.product_size_ar = product_size_ar;
    }

    public String getProduct_size_en() {
        return product_size_en;
    }

    public void setProduct_size_en(String product_size_en) {
        this.product_size_en = product_size_en;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }

    public double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(int product_price) {
        this.product_price = product_price;
    }

    public double getProduct_total_price() {
        return product_total_price;
    }

    public void setProduct_total_price(double product_total_price) {
        this.product_total_price = product_total_price;
    }

    public void setProduct_price(double product_price) {
        this.product_price = product_price;
    }

    public AlternativeProductItem getAlternativeProductItem() {
        return alternativeProductItem;
    }

    public void setAlternativeProductItem(AlternativeProductItem alternativeProductItem) {
        this.alternativeProductItem = alternativeProductItem;
    }


}
