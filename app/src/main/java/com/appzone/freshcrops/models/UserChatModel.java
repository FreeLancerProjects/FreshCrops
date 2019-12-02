package com.appzone.freshcrops.models;

import java.io.Serializable;

public class UserChatModel implements Serializable {
    private int id;
    private int room_id;
    private String name;
    private String phone;
    private String alter_phone="";
    private String image;
    private String user_type;
    private double rate;

    public UserChatModel(int id, int room_id, String name, String phone, String image, String user_type, double rate) {
        this.id = id;
        this.room_id = room_id;
        this.name = name;
        this.phone = phone;
        this.image = image;
        this.user_type = user_type;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getAlter_phone() {
        return alter_phone;
    }

    public void setAlter_phone(String alter_phone) {
        this.alter_phone = alter_phone;
    }
}
