package com.vutrannguyen_k214111980_k21411ca.model;

public class Cart {
    private int productId;
    private String productName;
    private String categoryId;
    private double productPrice;
    private double salePrice;
    private int inventory;
    private String thumbnail;
    private String accountId;

    public Cart() {
        // Default constructor required for calls to DataSnapshot.getValue(Cart.class)
    }

    public Cart(int productId, String productName, String categoryId, double productPrice, double salePrice, int inventory, String thumbnail, String accountId) {
        this.productId = productId;
        this.productName = productName;
        this.categoryId = categoryId;
        this.productPrice = productPrice;
        this.salePrice = salePrice;
        this.inventory = inventory;
        this.thumbnail = thumbnail;
        this.accountId = accountId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
