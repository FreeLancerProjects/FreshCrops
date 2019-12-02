package com.appzone.freshcrops.models;

import java.io.Serializable;

public class PageModel implements Serializable {
    int page;

    public PageModel(int page) {
        this.page = page;
    }

    public int getStatus() {
        return page;
    }
}
