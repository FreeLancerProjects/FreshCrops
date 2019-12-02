package com.appzone.freshcrops.models;

import java.io.Serializable;

public class Products implements Serializable {
    private int product_id;
    private int status;

    public Products(int product_id, int status) {
        this.product_id = product_id;
        this.status = status;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
