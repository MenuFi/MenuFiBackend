package com.menufi.backend.springboot.metrics;

import com.google.common.collect.ImmutableList;
import com.menufi.backend.springboot.sql.Querier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MetricsService {

    @Autowired
    private Querier querier;

    public Collection<MenuItemClick> getMenuItemClicks(int menuItemId) {
        // TODO: Implement
        return ImmutableList.of();
    }

    public boolean addMenuItemClick(MenuItemClick menuItemClick) {
        // TODO: Implement
        return false;
    }
}
