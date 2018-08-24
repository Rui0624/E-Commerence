package com.example.android.onlineshopping.dataItem;

public class OrderInfo {

    private String pid;
    private String pname;
    private int quantity;
    private String prize;
    private String image;

    public OrderInfo(String pid, String pname, int quantity, String prize, String image) {
        this.pid = pid;
        this.pname = pname;
        this.quantity = quantity;
        this.prize = prize;
        this.image = image;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
