package com.appzone.freshcrops.models;

import java.io.Serializable;

public class MessageModel implements Serializable {
    private int id;
    private int room_id;
    private int sender_id;
    private int receiver_id;
    private String msg;
    private int type;
    private long msg_time;

    public MessageModel() {
    }

    public MessageModel(int room_id, int sender_id, int receiver_id, String msg, int type, long msg_time) {
        this.room_id = room_id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.msg = msg;
        this.type = type;
        this.msg_time = msg_time;
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

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getMsg_time() {
        return msg_time;
    }

    public void setMsg_time(long msg_time) {
        this.msg_time = msg_time;
    }
}
