package com.appzone.freshcrops.models;

import java.io.Serializable;
import java.util.List;

public class ProductPaginationModel implements Serializable {

    private int current_page;
    private List<MainCategory.Products> data;

    public int getCurrent_page() {
        return current_page;
    }

    public List<MainCategory.Products> getData() {
        return data;
    }
}
