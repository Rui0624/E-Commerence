package com.example.android.onlineshopping.dataItem;

public class ProductsListItem {

    private String id;
    private String pname;
    private String quantity;
    private String price;
    private String discription;
    private String image;

    public ProductsListItem(String id, String pname, String quantity, String price, String discription, String image) {
        this.id = id;
        this.pname = pname;
        this.quantity = quantity;
        this.price = price;
        this.discription = discription;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
