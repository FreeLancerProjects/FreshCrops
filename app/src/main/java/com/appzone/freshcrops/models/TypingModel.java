package com.appzone.freshcrops.models;

import java.io.Serializable;

public class TypingModel implements Serializable {

    private int room_id;
    private int sender_id;
    private int receiver_id;
    private int status;

    public TypingModel(int room_id, int receiver_id, int status) {
        this.room_id = room_id;
        this.receiver_id = receiver_id;
        this.status = status;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }



    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
