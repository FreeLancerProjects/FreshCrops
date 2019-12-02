package com.appzone.freshcrops.models;

import java.io.Serializable;

public class DeliveryCostModel implements Serializable {

    private Delivery delivery;

    public Delivery getDelivery() {
        return delivery;
    }

    public class Delivery implements Serializable
    {
        private String more_2;
        private String less_2;

        public String getMore_2() {
            return more_2;
        }

        public String getLess_2() {
            return less_2;
        }
    }
}
