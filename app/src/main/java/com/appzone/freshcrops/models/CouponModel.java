package com.appzone.freshcrops.models;

import java.io.Serializable;

public class CouponModel implements Serializable {
   private int coupon_value;
   private int minimum_order_cost;
   private int client_point_cost;
   private CouponCodes coupon_codes;

    public int getCoupon_value() {
        return coupon_value;
    }

    public int getMinimum_order_cost() {
        return minimum_order_cost;
    }

    public int getClient_point_cost() {
        return client_point_cost;
    }

    public CouponCodes getCoupon_codes() {
        return coupon_codes;
    }

    public CouponModel(int coupon_value, int minimum_order_cost, int client_point_cost) {
        this.coupon_value = coupon_value;
        this.minimum_order_cost = minimum_order_cost;
        this.client_point_cost = client_point_cost;
    }

    public void setCoupon_codes(CouponCodes coupon_codes) {
        this.coupon_codes = coupon_codes;
    }

    public class CouponCodes implements Serializable
   {
       private String ar;
       private String en;

       public String getAr() {
           return ar;
       }

       public String getEn() {
           return en;
       }
   }
}
