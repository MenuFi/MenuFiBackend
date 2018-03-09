package com.menufi.backend.springboot.metrics;

import java.sql.Timestamp;

public class AddMenuItemClickRequest {
    private int menuItemId;
    private int userId;

    public AddMenuItemClickRequest(int menuItemClickId, int menuItemId, Timestamp timestamp, int userId) {
        this.menuItemId = menuItemId;
        this.userId = userId;
    }


    public int getMenuItemId() {
        return menuItemId;
    }

    public int getUserId() {
        return userId;
    }
}
