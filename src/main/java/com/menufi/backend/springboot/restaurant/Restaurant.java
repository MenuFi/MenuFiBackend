package com.menufi.backend.springboot.restaurant;

public class Restaurant {
    private final int restaurantId; // make it hashable later;
    private String name;
    private float price;
    private String pictureUri;

    public Restaurant(int restaurantId, String name, float price, String pictureUri) {
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

    public float getPrice() {
        return price;
    }

    public String getPictureUri() {
        return pictureUri;
    }
}
