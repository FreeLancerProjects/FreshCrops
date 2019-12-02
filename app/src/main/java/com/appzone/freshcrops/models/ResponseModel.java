package com.appzone.freshcrops.models;

import java.io.Serializable;

public class ResponseModel implements Serializable {
    private int code;
    private int success;
    public int getCode() {
        return code;
    }

    public int getSuccess() {
        return success;
    }
}
