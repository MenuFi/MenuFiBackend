package com.menufi.backend.springboot.menu;

public class MenuItem {
    public final int id; // make it hashable later
    public String name;
    public String imageUrl; //InetAddress in android
    public String description;
    public float price;
    public float rating;

    public MenuItem(int id, String name, String imageUrl, String description, float price, float rating) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.price = price;
        this.rating = rating;
    }
    // TODO: Constructor chaining
    // TODO: GET and SET METHODS

}
