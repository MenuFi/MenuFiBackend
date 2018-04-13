package com.menufi.backend.springboot.menu;

import com.google.common.collect.ImmutableList;
import com.menufi.backend.springboot.RestUtil;
import com.menufi.backend.springboot.login.LoginService;
import com.menufi.backend.springboot.metrics.MenuItemClick;
import com.menufi.backend.springboot.metrics.MetricsService;
import com.menufi.backend.springboot.sql.Querier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
public class MenuService {

    private static final String MENU_TABLE = "restaurant_menu_items";
    private static final List<String> GET_ITEM_COLUMNS = ImmutableList.of(
        "MenuItemId", "RestaurantId", "Name", "Price", "Calories", "Description", "Rating", "PictureUri"
    );

    private static final String INGREDIENTS_TABLE = "menu_item_ingredients";
    private static final List<String> GET_INGREDIENTS_COLUMNS = ImmutableList.of("Name");

    private static final String PREFERENCES_MAPPING_TABLE = "dietary_preferences_mapping";
    private static final List<String> GET_PREFERENCES_MAPPING_COLUMNS = ImmutableList.of("DietaryPreferenceId");

    private static final String PREFERENCES_TABLE = "dietary_preferences";
    private static final List<String> GET_PREFERENCES_COLUMNS = ImmutableList.of(
        "DietaryPreferenceId", "Name", "Type"
    );

    @Autowired
    private Querier querier;

    @Autowired
    private MetricsService metricsService;

    @Autowired
    private LoginService loginService;

    public boolean updateMenuItemRating(int menuItemId, String token, double newRating) {
        int userId = loginService.authenticateToken(token);
        Map<String, String> updateValues = new HashMap<>();
        updateValues.put("Rating", Double.toString(newRating));
        Map<String, String> whereClause = new HashMap<>();
        whereClause.put("MenuItemId", Integer.toString(menuItemId));
        whereClause.put("UserId", Integer.toString(userId));
        return querier.update(MENU_TABLE, updateValues, whereClause);
    }

    public boolean updateMenuItem(int restaurantId, int menuItemId, String token, MenuItem menuItem) {
        int userId = loginService.authenticateToken(token);
        Map<String, String> updateValues = MenuService.translateFromMenuItemForUpdate(menuItem);
        Map<String, String> whereClause = new HashMap<>();
        whereClause.put("RestaurantId", Integer.toString(restaurantId));
        whereClause.put("MenuItemId", Integer.toString(menuItemId));
        whereClause.put("UserId", Integer.toString(userId));
        if (querier.update(MENU_TABLE, updateValues, whereClause)) {
            if (updateIngredients(menuItem.getIngredients(), menuItemId)) {
                return updateDietaryPreferences(menuItem.getDietaryPreferences(), menuItemId);
            }
        }
        return false;
    }

    public boolean updateIngredients(String[] ingredients, int menuItemId) {
        Map<String, String> deleteWhere = new HashMap<>();
        deleteWhere.put("MenuItemId", Integer.toString(menuItemId));
        if (querier.delete(INGREDIENTS_TABLE, deleteWhere)) {
            return addIngredients(ingredients, menuItemId);
        }
        return false;
    }

    public boolean updateDietaryPreferences(int[] dietaryPreferences, int menuItemId) {
        Map<String, String> deleteWhere = new HashMap<>();
        deleteWhere.put("MenuItemId", Integer.toString(menuItemId));
        if (querier.delete(PREFERENCES_MAPPING_TABLE, deleteWhere)) {
            return addDietaryPreferences(dietaryPreferences, menuItemId);
        }
        return false;
    }

    public boolean addIngredients(String[] ingredients, int menuItemId) {
        boolean succeeded = true;
        List<Map<String, String>> ingredientsValues = MenuService.translateFromIngredients(ingredients, menuItemId);
        for (Map<String, String> ingredientsRow : ingredientsValues) {
            succeeded = succeeded && querier.insert(INGREDIENTS_TABLE, ingredientsRow);
        }
        return succeeded;
    }

    public boolean addDietaryPreferences(int[] dietaryPreferences, int menuItemId) {
        boolean succeeded = true;
        List<DietaryPreference> allDietaryPreferences = getAllDietaryPreferences();
        List<Integer> validPreferences = new ArrayList<>();
        for (int preferenceId : dietaryPreferences) {
            try (Stream<DietaryPreference> allDietaryPreferencesStream = allDietaryPreferences.stream()) {
                if (allDietaryPreferencesStream.anyMatch((dietaryPreference) -> dietaryPreference.getDietaryPreferenceId() == preferenceId)) {
                    validPreferences.add(preferenceId);
                } else {
                    succeeded = false;
                }
                allDietaryPreferencesStream.close();
            }
        }
        List<Map<String, String>> preferenceValues = MenuService.translateFromPreferenceIds(validPreferences, menuItemId);
        for (Map<String, String> preferenceRow : preferenceValues) {
            succeeded = succeeded && querier.insert(PREFERENCES_MAPPING_TABLE, preferenceRow);
        }
        return succeeded;
    }

    public int addMenuItem(AddMenuItemRequest addMenuItemRequest, String token) {
        String userId = RestUtil.parseAuthHeader(token);
        if (querier.insert(MENU_TABLE, MenuService.translateFromAddRequest(addMenuItemRequest)) && userId != null) {
            List<Map<String, String>> result = querier.query(MENU_TABLE, ImmutableList.of("MenuItemId"));
            if (!result.isEmpty()) {
                int newMenuItemId = Integer.parseInt(result.get(result.size() - 1).get("MenuItemId"));
                addIngredients(addMenuItemRequest.getIngredients(), newMenuItemId);
                addDietaryPreferences(addMenuItemRequest.getDietaryPreferences(), newMenuItemId);
                return newMenuItemId;
            }
        }
        return -1;
    }

    public MenuItem getMenuItem(int restaurantId, int menuItemId, String token) {
        int userId = loginService.authenticateToken(token);

        Map<String, String> whereClause = new HashMap<>();
        whereClause.put("RestaurantId", Integer.toString(restaurantId));
        whereClause.put("MenuItemId", Integer.toString(menuItemId));
        whereClause.put("UserId", Integer.toString(userId));

        List<Map<String, String>> result = querier.queryWhere(MENU_TABLE, GET_ITEM_COLUMNS, whereClause);

        if (!result.isEmpty()) {
            MenuItem mappedMenuItem = MenuService.translateToMenuItem(result.get(0), 1);
            if (mappedMenuItem != null) {
                mappedMenuItem.setIngredients(getIngredients(mappedMenuItem.getMenuItemId()));
                mappedMenuItem.setDietaryPreferences(getDietaryPreferences(mappedMenuItem.getMenuItemId()));
                return mappedMenuItem;
            }
        }
        return null;
    }

    public Collection<MenuItem> getAllMenuItems(int restaurantId) {
        List<MenuItem> allMenuItems = new ArrayList<>();

        Map<String, String> whereClause = new HashMap<>();
        whereClause.put("RestaurantId", Integer.toString(restaurantId));

        List<Map<String, String>> result = querier.queryWhere(MENU_TABLE, GET_ITEM_COLUMNS, whereClause);
        for (Map<String, String> entry : result) {
            MenuItem mappedMenuItem = MenuService.translateToMenuItem(entry, 0);
            Collection<MenuItemClick> clicks = metricsService.getMenuItemClicks(mappedMenuItem.getMenuItemId());
            mappedMenuItem.setPopularity(clicks.size());
            if (mappedMenuItem != null) {
                mappedMenuItem.setIngredients(getIngredients(mappedMenuItem.getMenuItemId()));
                mappedMenuItem.setDietaryPreferences(getDietaryPreferences(mappedMenuItem.getMenuItemId()));
                allMenuItems.add(mappedMenuItem);
            }
        }
        Collections.sort(allMenuItems);
        Collections.reverse(allMenuItems);
        for (int i = 0; i < allMenuItems.size(); i += 1) {
            allMenuItems.get(i).setPopularity(i + 1);
        }
        return allMenuItems;
    }

    public String[] getIngredients(int menuItemId) {
        List<String> ingredients = new ArrayList<>();

        Map<String, String> whereClause = new HashMap<>();
        whereClause.put("MenuItemId", Integer.toString(menuItemId));
        List<Map<String, String>> result = querier.queryWhere(INGREDIENTS_TABLE, GET_INGREDIENTS_COLUMNS, whereClause);

        String key = GET_INGREDIENTS_COLUMNS.get(0);
        for (Map<String, String> entry : result) {
            if (entry.containsKey(key)) {
                ingredients.add(entry.get(key));
            }
        }
        return ingredients.toArray(new String[0]);
    }

    public int[] getDietaryPreferences(int menuItemId) {
        List<Integer> dietaryPreferences = new ArrayList<>();

        Map<String, String> whereClause = new HashMap<>();
        whereClause.put("MenuItemId", Integer.toString(menuItemId));
        List<Map<String, String>> result = querier.queryWhere(PREFERENCES_MAPPING_TABLE, GET_PREFERENCES_MAPPING_COLUMNS, whereClause);

        String key = GET_PREFERENCES_MAPPING_COLUMNS.get(0);
        for (Map<String, String> entry : result) {
            if (entry.containsKey(key)) {
                dietaryPreferences.add(Integer.parseInt(entry.get(key)));
            }
        }

        int[] resultPreferences = new int[dietaryPreferences.size()];
        for (int i = 0; i < resultPreferences.length; i += 1) {
            resultPreferences[i] = dietaryPreferences.get(i);
        }
        return resultPreferences;
    }

    public List<DietaryPreference> getAllDietaryPreferences() {
        List<DietaryPreference> allDietaryPreferences = new ArrayList<>();
        List<Map<String, String>> result = querier.query(PREFERENCES_TABLE, GET_PREFERENCES_COLUMNS);
        for (Map<String, String> entry : result) {
            DietaryPreference mappedDietaryPreference = MenuService.translateToDietaryPreference(entry);
            if (mappedDietaryPreference != null) {
                allDietaryPreferences.add(mappedDietaryPreference);
            }
        }
        return allDietaryPreferences;
    }

    public static List<Map<String, String>> translateFromPreferenceIds(Collection<Integer> preferenceIds, int menuItemId) {
        List<Map<String, String>> preferenceValues = new ArrayList<>();
        for (int preferenceId : preferenceIds) {
            Map<String, String> preferenceRow = new HashMap<>();
            preferenceRow.put("MenuItemId", Integer.toString(menuItemId));
            preferenceRow.put("DietaryPreferenceID", Integer.toString(preferenceId));
            preferenceValues.add(preferenceRow);
        }
        return preferenceValues;
    }

    public static List<Map<String, String>> translateFromIngredients(String[] ingredients, int menuItemId) {
        List<Map<String, String>> ingredientsValues = new ArrayList<>();
        for (String ingredient : ingredients) {
            Map<String, String> ingredientsRow = new HashMap<>();
            ingredientsRow.put("MenuItemId", Integer.toString(menuItemId));
            ingredientsRow.put("Name", ingredient);
            ingredientsValues.add(ingredientsRow);
        }
        return ingredientsValues;
    }

    public static Map<String, String> translateFromAddRequest(AddMenuItemRequest addMenuItemRequest) {
        Map<String, String> addMenuItemValues = new HashMap<>();
        addMenuItemValues.put("RestaurantId", Integer.toString(addMenuItemRequest.getRestaurantId()));
        addMenuItemValues.put("Name", addMenuItemRequest.getName());
        addMenuItemValues.put("Price", Double.toString(addMenuItemRequest.getPrice()));
        addMenuItemValues.put("Calories", Integer.toString(addMenuItemRequest.getCalories()));
        addMenuItemValues.put("Description", addMenuItemRequest.getDescription());
        addMenuItemValues.put("Rating", Double.toString(addMenuItemRequest.getRating()));
        addMenuItemValues.put("PictureUri", addMenuItemRequest.getPictureUri());
        return addMenuItemValues;
    }

    public static Map<String, String> translateFromMenuItemForUpdate(MenuItem menuItem) {
        Map<String, String> updateMenuItemValues = new HashMap<>();
        updateMenuItemValues.put("Name", menuItem.getName());
        updateMenuItemValues.put("Price", Double.toString(menuItem.getPrice()));
        updateMenuItemValues.put("Calories", Integer.toString(menuItem.getCalories()));
        updateMenuItemValues.put("Description", menuItem.getDescription());
        updateMenuItemValues.put("Rating", Double.toString(menuItem.getRating()));
        updateMenuItemValues.put("PictureUri", menuItem.getPictureUri());
        return updateMenuItemValues;
    }

    public static MenuItem translateToMenuItem(Map<String, String> entry, int popularity) {
        boolean canTranslate = true;
        for (String column : GET_ITEM_COLUMNS) {
            canTranslate = canTranslate && entry.containsKey(column);
        }
        return !canTranslate ? null : new MenuItem(
                Integer.parseInt(entry.get("MenuItemId")),
                Integer.parseInt(entry.get("RestaurantId")),
                entry.get("Name"),
                Double.parseDouble(entry.get("Price")),
                Integer.parseInt(entry.get("Calories")),
                entry.get("Description"),
                Double.parseDouble(entry.get("Rating")),
                entry.get("PictureUri"),
                popularity
        );
    }

    public static DietaryPreference translateToDietaryPreference(Map<String, String> entry) {
        boolean canTranslate = true;
        for (String column : GET_PREFERENCES_COLUMNS) {
            canTranslate = canTranslate && entry.containsKey(column);
        }
        return !canTranslate ? null : new DietaryPreference(
            Integer.parseInt(entry.get("DietaryPreferenceId")),
            entry.get("Name"),
            Integer.parseInt(entry.get("Type"))
        );
    }
}