package com.menufi.backend.springboot.metrics;

import java.sql.Timestamp;

public class AddMenuItemClickRequest {
    private int menuItemId;
    private int userId;

    public AddMenuItemClickRequest() { }

    public AddMenuItemClickRequest(int menuItemId, int userId) {
        this.menuItemId = menuItemId;
        this.userId = userId;
    }


    public int getMenuItemId() {
        return menuItemId;
    }

    public int getUserId() {
        return userId;
    }

    public void setMenuItemId() {
        this.menuItemId = menuItemId;
    }

    public void setUserId() {
        this.userId = userId;
    }
}
