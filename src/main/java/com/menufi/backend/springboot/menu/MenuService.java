package com.menufi.backend.springboot.menu;

import com.menufi.backend.sql.Querier;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

public class MenuService {

    @Autowired
    private Querier querier;

    public MenuItem addMenuItem(AddMenuItemRequest addMenuItemRequest) {
        throw new UnsupportedOperationException();
    }

    public Collection<MenuItem> getMenuItems(int restaurantId) {
        throw new UnsupportedOperationException();
    }
}

