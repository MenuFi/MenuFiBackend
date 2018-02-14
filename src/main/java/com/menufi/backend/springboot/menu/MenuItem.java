package com.menufi.backend.springboot.menu;

public class MenuItem {
    private int menuItemId;
    private int restaurantId;
    private String name;
    private double price;
    private int calories;
    private String description;
    private double rating;
    private String pictureUri;
    private String[] ingredients;
    private int[] dietaryPreferences;

    public MenuItem() {}

    public MenuItem(int menuItemId, int restaurantId, String name, double price, int calories, String description, double rating, String pictureUri) {
        this.menuItemId = menuItemId;
        this.restaurantId = restaurantId;
        this.name = name;
        this.price = price;
        this.calories = calories;
        this.description = description;
        this.rating = rating;
        this.pictureUri = pictureUri;
        this.ingredients = new String[0];
        this.dietaryPreferences = new int[0];
    }
    // TODO: Constructor chaining

    public int getMenuItemId() {
        return menuItemId;
    }

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

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public void setDietaryPreferences(int[] dietaryPreferences) {
        this.dietaryPreferences = dietaryPreferences;
    }
}
