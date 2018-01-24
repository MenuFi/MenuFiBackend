package com.menufi.backend.springboot.menu;

import com.menufi.backend.springboot.sql.Querier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
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

