package com.menufi.backend.springboot.menu;

import java.util.Comparator;

public class MenuItem implements Comparable<MenuItem>{
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
    private int popularity;

    public MenuItem() {}

    public MenuItem(int menuItemId, int restaurantId, String name, double price, int calories, String description, double rating, String pictureUri, int popularity) {
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
        this.popularity = popularity;
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

    public int getPopularity() { return popularity; }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public void setDietaryPreferences(int[] dietaryPreferences) {
        this.dietaryPreferences = dietaryPreferences;
    }

    @Override
    public int compareTo(MenuItem o) {
        if (o == null) {
            return 1;
        }
        int cmp = Integer.compare(this.getPopularity(), o.getPopularity());
        if (cmp == 0) {
            cmp = Double.compare(this.getRating(), o.getRating());
        }
        return cmp;
    }
}
