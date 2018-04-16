package com.menufi.backend.springboot.restaurant;

import java.sql.Timestamp;

public class AddRestaurantRequest {
    private String name;
    private double price;
    private String pictureUri;

    public AddRestaurantRequest() { }

    public AddRestaurantRequest(String name, double price, String pictureUri) {
        this.name = name;
        this.price = price;
        this.pictureUri = pictureUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPictureUri() {
        return pictureUri;
    }

    public void setPictureUri(String pictureUri) {
        this.pictureUri = pictureUri;
    }
}
