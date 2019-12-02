package com.appzone.freshcrops.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OrdersModel implements Serializable{

    private List<Order> data;

    public List<Order> getData() {
        return data;
    }

    public class Order implements Serializable
    {
        private int id;
        private double lat;
        @SerializedName("long")
        private double lng;
        private String address;
        private String street;
        private String client_phone;
        private String client_alternative_phone;
        private String note;
        private double total;
        private double coupon_value;
        private String coupon_code;
        private int time_type;
        private int payment_method;
        private int status;
        private String created_at;
        private int discount_by_use;
        private double total_discount;
        private double discount_point;
        private double delivery_cost;
        private long milli_time;
        private long accepted_time;
        private String bill_image;
        private int chat_room_id;
        private Client client;
        private Delegate delegate;
        private List<Products> products;

        public int getId() {
            return id;
        }

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }

        public String getAddress() {
            return address;
        }

        public String getClient_phone() {
            return client_phone;
        }

        public String getClient_alternative_phone() {
            return client_alternative_phone;
        }

        public String getStreet() {
            return street;
        }

        public String getNote() {
            return note;
        }

        public double getTotal() {
            return total;
        }

        public double getCoupon_value() {
            return coupon_value;
        }

        public String getCoupon_code() {
            return coupon_code;
        }

        public int getTime_type() {
            return time_type;
        }

        public int getPayment_method() {
            return payment_method;
        }

        public int getStatus() {
            return status;
        }

        public int getDiscount_by_use() {
            return discount_by_use;
        }

        public double getTotal_discount() {
            return total_discount;
        }

        public double getDiscount_point() {
            return discount_point;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getBill_image() {
            return bill_image;
        }

        public int getChat_room_id() {
            return chat_room_id;
        }

        public double getDelivery_cost() {
            return delivery_cost;
        }

        public long getMilli_time() {
            return milli_time;
        }

        public long getAccepted_time() {
            return accepted_time;
        }

        public Client getClient() {
            return client;
        }

        public Delegate getDelegate() {
            return delegate;
        }

        public List<Products> getProducts() {
            return products;
        }
    }

    public class Client implements Serializable
    {
        private int id;
        private String name;
        private String phone;
        private double rate;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public double getRate() {
            return rate;
        }
    }

    public class Delegate implements Serializable
    {
        private int id;
        private String name;
        private String phone;
        private String avatar;
        private double rate;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public String getAvatar() {
            return avatar;
        }

        public double getRate() {
            return rate;
        }
    }

    public class Products implements Serializable
    {
        int id;
        private int feature_id;
        private int quantity;
        private double total;
        private Product_Price product_price;
        private Product product;
        private Feature feature;
        private Products alternative;


        public int getId() {
            return id;
        }

        public int getFeature_id() {
            return feature_id;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getTotal() {
            return total;
        }

        public Product_Price getProduct_price() {
            return product_price;
        }

        public Product getProduct() {
            return product;
        }

        public Feature getFeature() {
            return feature;
        }

        public Products getAlternative() {
            return alternative;
        }
    }

    public class Product_Price implements Serializable
    {
        private int id;
        private double net_price;
        private String size_ar;
        private String size_en;

        public int getId() {
            return id;
        }

        public double getNet_price() {
            return net_price;
        }

        public String getSize_ar() {
            return size_ar;
        }

        public String getSize_en() {
            return size_en;
        }
    }

    public class Product implements Serializable
    {
        private int id;
        private String name_ar;
        private String name_en;
        private List<String>image;
        private String main_category_id;
        private String sub_category_id;

        public int getId() {
            return id;
        }

        public String getName_ar() {
            return name_ar;
        }

        public String getName_en() {
            return name_en;
        }

        public List<String> getImage() {
            return image;
        }

        public String getMain_category_id() {
            return main_category_id;
        }

        public String getSub_category_id() {
            return sub_category_id;
        }
    }

    public class Feature implements Serializable
    {
        private int feature_id;
        private double discount;

        public int getFeature_id() {
            return feature_id;
        }

        public double getDiscount() {
            return discount;
        }
    }
}
