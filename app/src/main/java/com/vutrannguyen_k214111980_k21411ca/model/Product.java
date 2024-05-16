package com.vutrannguyen_k214111980_k21411ca.model;

public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private double salePrice;
    private String categoryId;
    private byte[] thumbnail;
    private int inventory;

    // Constructor
    public Product(int id, String name, String description, double price, double salePrice,
                   String categoryId, byte[] thumbnail, int inventory) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.salePrice = salePrice;
        this.categoryId = categoryId;
        this.thumbnail = thumbnail;
        this.inventory = inventory;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public int getInventory() {
        return inventory;
    }
}