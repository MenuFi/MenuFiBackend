package com.menufi.backend.springboot.metrics;

import com.google.common.collect.ImmutableList;
import com.menufi.backend.springboot.menu.MenuItem;
import com.menufi.backend.springboot.menu.MenuService;
import com.menufi.backend.springboot.sql.Querier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Metrics;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class MetricsService {

    @Autowired
    private Querier querier;

    private static final String METRICS_TABLE = "METRICS_TABLE";
    private static final List<String> GET_METRICS_COLUMNS = ImmutableList.of("MenuItemClickId", "MenuItemId", "Timestamp", "UserId");

    public Collection<MenuItemClick> getMenuItemClicks(int menuItemId) {
        // TODO: Implement
        Collection<MenuItemClick> allMenuItemClicks = new ArrayList<>();
        Map<String, String> whereClause = new HashMap<>();
//        whereClause.put("RestaurantID", Integer.toString(restaurantId))
//        whereClause.put("MenuItemClickId", Integer.toString());
        whereClause.put("MenuItemId", Integer.toString(menuItemId));
        List<Map<String, String>> result = querier.queryWhere(METRICS_TABLE, GET_METRICS_COLUMNS, whereClause);
        for (Map<String, String> entry : result) {
            MenuItemClick mappedItemClick = MetricsService.translateToMenuItemClick(result.get(0));
            if (mappedItemClick != null) {
                allMenuItemClicks.add(mappedItemClick);
            }
        }
        return allMenuItemClicks;
    }

    public boolean addMenuItemClick(MenuItemClick menuItemClick) {
        // TODO: Implement
        return false;
    }

    public static MenuItemClick translateToMenuItemClick(Map<String, String> entry) {
        boolean canTranslate = true;
        for (String column : GET_METRICS_COLUMNS) {
            canTranslate = canTranslate && entry.containsKey(column);
        }
        return !canTranslate ? null : new MenuItemClick(
                Integer.parseInt(entry.get("MenuItemClickId")),
                Integer.parseInt(entry.get("MenuItemId")),
                Timestamp.valueOf(entry.get("Timestamp")),
                Integer.parseInt(entry.get("UserId"))
        );
    }
}
