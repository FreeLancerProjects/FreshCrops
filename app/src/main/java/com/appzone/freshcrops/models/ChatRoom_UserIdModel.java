package com.appzone.freshcrops.models;

import java.io.Serializable;

public class ChatRoom_UserIdModel implements Serializable {
    private int chatUserId;
    private int roomId;

    public ChatRoom_UserIdModel(int chatUserId, int roomId) {
        this.chatUserId = chatUserId;
        this.roomId = roomId;
    }

    public int getChatUserId() {
        return chatUserId;
    }

    public int getRoomId() {
        return roomId;
    }
}
