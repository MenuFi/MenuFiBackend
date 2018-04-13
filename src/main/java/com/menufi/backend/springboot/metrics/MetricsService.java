package com.menufi.backend.springboot.metrics;

import com.google.common.collect.ImmutableList;
import com.menufi.backend.springboot.login.LoginService;
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

    @Autowired
    private LoginService loginService;

    private static final String MENU_METRICS_TABLE = "menu_item_clicks";
    private static final List<String> GET_MENU_METRICS_COLUMNS = ImmutableList.of("MenuItemClickId", "MenuItemId", "Timestamp", "UserId");

    public Collection<MenuItemClick> getMenuItemClicks(int menuItemId, String token) {
        int userId = loginService.authenticateToken(token);
        Collection<MenuItemClick> allMenuItemClicks = new ArrayList<>();
        Map<String, String> whereClause = new HashMap<>();
        whereClause.put("MenuItemId", Integer.toString(menuItemId));
        List<Map<String, String>> result = querier.queryWhere(MENU_METRICS_TABLE, GET_MENU_METRICS_COLUMNS, whereClause);
        for (Map<String, String> entry : result) {
            MenuItemClick mappedItemClick = MetricsService.translateToMenuItemClick(entry);
            if (mappedItemClick != null) {
                allMenuItemClicks.add(mappedItemClick);
            }
        }
        return allMenuItemClicks;
    }

    public int addMenuItemClick(int menuItemId, String userToken) {
        int userId = loginService.authenticateToken(userToken);
        if (querier.insert(MENU_METRICS_TABLE, MetricsService.translateFromAddRequest(new AddMenuItemClickRequest(menuItemId, userId)))) {
            List<Map<String, String>> result = querier.query(MENU_METRICS_TABLE, ImmutableList.of("MenuItemClickId"));
            if (!result.isEmpty()) {
                int newMenuItemClickId = Integer.parseInt(result.get(result.size()-1).get("MenuItemClickId"));
                return newMenuItemClickId;
            }
        }
        return -1;
    }

    public static MenuItemClick translateToMenuItemClick(Map<String, String> entry) {
        boolean canTranslate = true;
        for (String column : GET_MENU_METRICS_COLUMNS) {
            canTranslate = canTranslate && entry.containsKey(column);
        }
        return !canTranslate ? null : new MenuItemClick(
                Integer.parseInt(entry.get("MenuItemClickId")),
                Integer.parseInt(entry.get("MenuItemId")),
                Timestamp.valueOf(entry.get("Timestamp")),
                Integer.parseInt(entry.get("UserId"))
        );
    }

    public static Map<String, String> translateFromAddRequest(AddMenuItemClickRequest addMenuItemClickRequest) {
        Map<String, String> addMenuItemClickValues = new HashMap<>();
        addMenuItemClickValues.put("MenuItemId", Integer.toString(addMenuItemClickRequest.getMenuItemId()));
        addMenuItemClickValues.put("UserId", Integer.toString(addMenuItemClickRequest.getUserId()));
        return addMenuItemClickValues;
    }
}
