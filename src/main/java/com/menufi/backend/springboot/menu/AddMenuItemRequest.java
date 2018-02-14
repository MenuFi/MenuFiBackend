package com.menufi.backend.springboot.menu;

public class AddMenuItemRequest {
    private int restaurantId;
    private String name;
    private double price;
    private int calories;
    private String description;
    private double rating;
    private String pictureUri;
    private String[] ingredients;
    private int[] dietaryPreferences;

    public AddMenuItemRequest() {}

    public AddMenuItemRequest(int restaurantId, String name, double price, int calories, String description, double rating, String pictureUri, String[] ingredients, int[] dietaryPreferences) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.price = price;
        this.calories = calories;
        this.description = description;
        this.rating = rating;
        this.pictureUri = pictureUri;
        this.ingredients = ingredients;
        this.dietaryPreferences = dietaryPreferences;
    }
    // TODO: Constructor chaining
    public int getRestaurantId() {
        return restaurantId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getCalories() {
        return calories;
    }

    public String getDescription() {
        return description;
    }

    public double getRating() {
        return rating;
    }

    public String getPictureUri() {
        return pictureUri;
    }

    public String[] getIngredients() { return ingredients; }

    public int[] getDietaryPreferences() { return dietaryPreferences; }

    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }
}

