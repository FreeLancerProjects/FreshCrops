package com.appzone.freshcrops.models;

import java.io.Serializable;

public class ProductSize_OfferModel implements Serializable {
    private int id;
    private boolean isOffer;
    private String discount;
    private String price_before_discount;
    private String price_after_discount;
    private int feature_id;
    private String ar_name;
    private String en_name;



    public int getId() {
        return id;
    }

    public boolean isOffer() {
        return isOffer;
    }

    public String getDiscount() {
        return discount;
    }

    public String getPrice_before_discount() {
        return price_before_discount;
    }

    public String getPrice_after_discount() {
        return price_after_discount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFeature_id() {
        return feature_id;
    }

    public void setFeature_id(int feature_id) {
        this.feature_id = feature_id;
    }

    public void setOffer(boolean offer) {
        isOffer = offer;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public void setPrice_before_discount(String price_before_discount) {
        this.price_before_discount = price_before_discount;
    }

    public void setPrice_after_discount(String price_after_discount) {
        this.price_after_discount = price_after_discount;
    }

    public String getAr_name() {
        return ar_name;
    }

    public void setAr_name(String ar_name) {
        this.ar_name = ar_name;
    }

    public String getEn_name() {
        return en_name;
    }

    public void setEn_name(String en_name) {
        this.en_name = en_name;
    }
}
