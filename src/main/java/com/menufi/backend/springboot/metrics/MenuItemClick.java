package com.menufi.backend.springboot.metrics;

import java.sql.Timestamp;

public class MenuItemClick {
    private int menuItemClickId;
    private int menuItemId;
    private Timestamp timestamp;
    private int userId;

    public MenuItemClick(int menuItemClickId, int menuItemId, Timestamp timestamp, int userId) {
        this.menuItemClickId = menuItemClickId;
        this.menuItemId = menuItemId;
        this.timestamp = timestamp;
        this.userId = userId;
    }

    public int getMenuItemClickId() {
        return menuItemClickId;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public int getUserId() {
        return userId;
    }
}
