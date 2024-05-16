package com.vutrannguyen_k214111980_k21411ca.model;

public class Category {
    private  String id;
    private  String name;
    private byte[] image;


    public Category(String id, String name, byte[] image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public byte[] getImage() {
        return image;
    }
}

