package com.appzone.freshcrops.models;

import java.io.Serializable;
import java.util.List;

public class WeekOfferModel implements Serializable {

    private List<Offer> data;

    public List<Offer> getData() {
        return data;
    }

    public class Offer implements Serializable
    {
        private int id ;
        private String name_ar;
        private String name_en;
        private String image;
        private String active;

        public int getId() {
            return id;
        }

        public String getName_ar() {
            return name_ar;
        }

        public String getName_en() {
            return name_en;
        }

        public String getImage() {
            return image;
        }

        public String getActive() {
            return active;
        }
    }
}
