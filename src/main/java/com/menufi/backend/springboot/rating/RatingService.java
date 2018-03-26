package com.menufi.backend.springboot.rating;

import com.google.common.collect.ImmutableList;
import com.menufi.backend.springboot.login.LoginService;
import com.menufi.backend.springboot.sql.Querier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RatingService {

    private static final String RATING_ITEM_TABLE = "menu_item_ratings";
    private static final List<String> RATING_ITEM_COLUMNS = ImmutableList.of("Rating");

    @Autowired
    private Querier querier;

    @Autowired
    private LoginService loginService;

    public boolean putMenuItemRating(int menuItemId, String token, double rating) {
        int userId = loginService.authenticateToken(token);
        Map<String, String> deleteWhere = new HashMap<>();
        deleteWhere.put("MenuItemId", Integer.toString(menuItemId));
        deleteWhere.put("UserId", Integer.toString(userId));
        querier.delete(RATING_ITEM_TABLE, deleteWhere);
        return addMenuItemRating(menuItemId, token, rating);
    }

    public boolean addMenuItemRating(int menuItemId, String token, double rating) {
        int userId = loginService.authenticateToken(token);
        Map<String, String> values = new HashMap<>();
        values.put("MenuItemId", Integer.toString(menuItemId));
        values.put("UserId", Integer.toString(userId));
        values.put("Rating", Double.toString(rating));
        return querier.insert(RATING_ITEM_TABLE, values);
    }

    public MenuItemRating getMenuItemRating(int menuItemId, String token) {
        int userId = loginService.authenticateToken(token);

        Map<String, String> whereClause = new HashMap<>();
        whereClause.put("MenuItemId", Integer.toString(menuItemId));
        whereClause.put("UserId", Integer.toString(userId));

        List<Map<String, String>> result = querier.queryWhere(RATING_ITEM_TABLE, RATING_ITEM_COLUMNS, whereClause);
        if (!result.isEmpty()) {
            double rating = Double.parseDouble(result.get(0).getOrDefault("Rating", "0"));
            return new MenuItemRating(menuItemId, userId, rating);
        }
        return null;
    }
}
