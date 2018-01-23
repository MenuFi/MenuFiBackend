package com.menufi.backend.springboot.restaurant;

public class Restaurant {
    public final int id; // make it hashable later;
    public String name;
    public String[] imageUrls;
    public String location;
    public FoodType foodType;

    public enum FoodType {
        ITALIAN,
        GREEK,
        FAST_FOOD,
        FINE_DINING,
        CHINESE,
        KOREAN,
        AMERICAN,
        FRENCH,
        THAI,
        MEXICAN,
        DESSERT;
    };

    public Restaurant(int id, String name, String[] imageUrls, String location, FoodType foodType) {
        this.id = id;
        this.name = name;
        this.imageUrls = imageUrls;
        this.location = location;
        this.foodType = foodType;
    }
//    TODO: Constructor chaining
//    TODO: GET and SET METHODS
}
