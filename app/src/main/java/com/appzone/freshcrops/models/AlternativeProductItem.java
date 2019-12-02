package com.appzone.freshcrops.models;

import java.io.Serializable;

public class AlternativeProductItem implements Serializable {
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


    public AlternativeProductItem(String product_image, int product_id, int feature_id, int product_price_id, String product_name_ar, String product_name_en, String product_size_ar, String product_size_en, int product_quantity, double product_price, double product_total_price) {
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

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }
}
