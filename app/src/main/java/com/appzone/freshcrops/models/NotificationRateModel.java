package com.appzone.freshcrops.models;

import java.io.Serializable;

public class NotificationRateModel implements Serializable {
    private String delegate_name;
    private String delegate_avatar;
    private int receiver_id;
    private int delegate_id;

    public NotificationRateModel(String delegate_name, String delegate_avatar, int receiver_id, int delegate_id) {
        this.delegate_name = delegate_name;
        this.delegate_avatar = delegate_avatar;
        this.receiver_id = receiver_id;
        this.delegate_id = delegate_id;
    }

    public String getDelegate_name() {
        return delegate_name;
    }

    public String getDelegate_avatar() {
        return delegate_avatar;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public int getDelegate_id() {
        return delegate_id;
    }
}
