package com.example.android.onlineshopping.dataItem;

public class CouponInfo {
    private String couponno;
    private String discount;


    public CouponInfo(String couponno, String discount) {
        this.couponno = couponno;
        this.discount = discount;
    }

    public String getCouponno() {
        return couponno;
    }

    public void setCouponno(String couponno) {
        this.couponno = couponno;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
