package com.menufi.backend.springboot.menu;

import com.menufi.backend.sql.Querier;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

public class MenuService {

    private Querier querier;

    @Autowired
    public MenuService(Querier querier) {
        this.querier = querier;
    }

    public MenuItem addMenuItem(AddMenuItemRequest addMenuItemRequest) {
        throw new UnsupportedOperationException();
    }

    public Collection<MenuItem> getMenuItems(int restaurantId) {
        throw new UnsupportedOperationException();
    }
}

