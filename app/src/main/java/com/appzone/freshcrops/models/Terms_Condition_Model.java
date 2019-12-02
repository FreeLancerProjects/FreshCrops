package com.appzone.freshcrops.models;

import java.io.Serializable;

public class Terms_Condition_Model implements Serializable {

    private Site_Terms_Conditions site_terms_conditions;

    public Site_Terms_Conditions getSite_terms_conditions() {
        return site_terms_conditions;
    }

    public class Site_Terms_Conditions implements Serializable
    {
        private String ar;
        private String en;

        public String getAr() {
            return ar;
        }

        public void setAr(String ar) {
            this.ar = ar;
        }

        public String getEn() {
            return en;
        }

        public void setEn(String en) {
            this.en = en;
        }
    }
 }
