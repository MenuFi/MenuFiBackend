package com.menufi.backend.springboot.menu;

import java.util.Collection;

public interface MenuService {
    MenuItem addMenuItem(AddMenuItemRequest addMenuItemRequest);
    Collection<MenuItem> getMenuItems(int restaurantId);
}

