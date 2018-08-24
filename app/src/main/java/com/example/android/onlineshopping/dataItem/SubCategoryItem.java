package com.example.android.onlineshopping.dataItem;

public class SubCategoryItem {

   private String scid;
   private String scname;
   private String scdiscription;
   private String scimageurl;

    public SubCategoryItem(String scid, String scname, String scdiscription, String scimageurl) {
        this.scid = scid;
        this.scname = scname;
        this.scdiscription = scdiscription;
        this.scimageurl = scimageurl;
    }

    public String getScid() {
        return scid;
    }

    public void setScid(String scid) {
        this.scid = scid;
    }

    public String getScname() {
        return scname;
    }

    public void setScname(String scname) {
        this.scname = scname;
    }

    public String getScdiscription() {
        return scdiscription;
    }

    public void setScdiscription(String scdiscription) {
        this.scdiscription = scdiscription;
    }

    public String getScimageurl() {
        return scimageurl;
    }

    public void setScimageurl(String scimageurl) {
        this.scimageurl = scimageurl;
    }
}
