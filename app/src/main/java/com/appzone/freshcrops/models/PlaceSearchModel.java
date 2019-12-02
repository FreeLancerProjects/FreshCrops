package com.appzone.freshcrops.models;

import java.io.Serializable;

public class PlaceSearchModel implements Serializable {
    private String name;
    private double lat;
    private double lng;

    public PlaceSearchModel(String name, double lat, double lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
