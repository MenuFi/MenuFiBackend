package com.menufi.backend.springboot.menu;

import com.google.common.collect.ImmutableList;
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

    public boolean updateMenuItem(int restaurantId, int menuItemId, MenuItem menuItem) {
        Map<String, String> updateValues = MenuService.translateFromMenuItemForUpdate(menuItem);
        Map<String, String> whereClause = new HashMap<>();
        whereClause.put("RestaurantId", Integer.toString(restaurantId));
        whereClause.put("MenuItemId", Integer.toString(menuItemId));
        return querier.update(MENU_TABLE, updateValues, whereClause);
    }

    public boolean addIngredients(String[] ingredients, int menuItemId) {
        boolean succeeded = true;
        List<Map<String, String>> ingredientsValues = MenuService.translateFromIngredients(ingredients, menuItemId);
        for (Map<String, String> ingredientsRow : ingredientsValues) {
            succeeded = succeeded && querier.insert(INGREDIENTS_TABLE, ingredientsRow);
        }
        return succeeded;
    }

    public boolean addDietaryPreferences(int[] dietaryPreferenes, int menuItemId) {
        boolean succeeded = true;
        Stream<DietaryPreference> allDietaryPreferences = getAllDietaryPreferences().stream();
        List<Integer> validPreferences = new ArrayList<>();
        for (int preferenceId : dietaryPreferenes) {
            if (allDietaryPreferences.anyMatch((dietaryPreference) -> dietaryPreference.getDietaryPreferenceId() == preferenceId )) {
                validPreferences.add(preferenceId);
            } else {
                succeeded = false;
            }
        }
        allDietaryPreferences.close();
        List<Map<String, String>> preferenceValues = MenuService.translateFromPreferenceIds(validPreferences, menuItemId);
        for (Map<String, String> preferenceRow : preferenceValues) {
            succeeded = succeeded && querier.insert(PREFERENCES_MAPPING_TABLE, preferenceRow);
        }
        return succeeded;
    }

    public int addMenuItem(AddMenuItemRequest addMenuItemRequest) {
        if (querier.insert(MENU_TABLE, MenuService.translateFromAddRequest(addMenuItemRequest))) {
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

    public MenuItem getMenuItem(int restaurantId, int menuItemId) {
        Map<String, String> whereClause = new HashMap<>();
        whereClause.put("RestaurantId", Integer.toString(restaurantId));
        whereClause.put("MenuItemId", Integer.toString(menuItemId));
        List<Map<String, String>> result = querier.queryWhere(MENU_TABLE, GET_ITEM_COLUMNS, whereClause);

        if (!result.isEmpty()) {
            MenuItem mappedMenuItem = MenuService.translateToMenuItem(result.get(0));
            if (mappedMenuItem != null) {
                mappedMenuItem.setIngredients(getIngredients(mappedMenuItem.getMenuItemId()));
                mappedMenuItem.setDietaryPreferences(getDietaryPreferences(mappedMenuItem.getMenuItemId()));
                return mappedMenuItem;
            }
        }
        return null;
    }

    public Collection<MenuItem> getAllMenuItems(int restaurantId) {
        Collection<MenuItem> allMenuItems = new ArrayList<>();

        Map<String, String> whereClause = new HashMap<>();
        whereClause.put("RestaurantId", Integer.toString(restaurantId));

        List<Map<String, String>> result = querier.queryWhere(MENU_TABLE, GET_ITEM_COLUMNS, whereClause);
        for (Map<String, String> entry : result) {
            MenuItem mappedMenuItem = MenuService.translateToMenuItem(entry);
            if (mappedMenuItem != null) {
                mappedMenuItem.setIngredients(getIngredients(mappedMenuItem.getMenuItemId()));
                mappedMenuItem.setDietaryPreferences(getDietaryPreferences(mappedMenuItem.getMenuItemId()));
                allMenuItems.add(mappedMenuItem);
            }
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

    public static MenuItem translateToMenuItem(Map<String, String> entry) {
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
                entry.get("PictureUri")
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