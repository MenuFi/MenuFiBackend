package com.menufi.backend.springboot.restaurant;

public class Restaurant {
    private final int restaurantId; // make it hashable later;
    private String name;
    private double price;
    private String pictureUri;

    public Restaurant(int restaurantId, String name, double price, String pictureUri) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.price = price;
        this.pictureUri = pictureUri;
    }
//    TODO: Constructor chaining

    public int getRestaurantId() {
        return restaurantId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getPictureUri() {
        return pictureUri;
    }
}
