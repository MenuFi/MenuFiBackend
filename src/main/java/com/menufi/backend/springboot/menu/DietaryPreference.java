package com.menufi.backend.springboot.menu;

public class DietaryPreference {
    private int dietaryPreferenceId;
    private String name;
    private int type;

    public DietaryPreference(int dietaryPreferenceId, String name, int type) {
        this.dietaryPreferenceId = dietaryPreferenceId;
        this.name = name;
        this.type = type;
    }

    public int getDietaryPreferenceId() {
        return dietaryPreferenceId;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }
}
