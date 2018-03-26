package com.menufi.backend.springboot.rating;

public class MenuItemRating {
    private int menuItemId;
    private int userId;
    private double rating;

    public MenuItemRating() { }

    public MenuItemRating(int menuItemId, int userId, double rating) {
        setMenuItemId(menuItemId);
        setUserId(userId);
        setRating(rating);
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
