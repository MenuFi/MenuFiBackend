package com.menufi.backend.springboot.restaurant;

import com.google.common.collect.ImmutableList;
import com.menufi.backend.springboot.login.LoginService;
import com.menufi.backend.springboot.sql.Querier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    private static final String RESTAURANT_TABLE = "restaurant_data";
    private static final List<String> RESTAURANT_DATA_COLUMNS = ImmutableList.of("RestaurantId", "Name", "Price", "PictureUri");

    @Autowired
    private Querier querier;

    @Autowired
    private LoginService loginService;

    public int getRestaurantId(String userToken) {
        return loginService.authenticateToken(userToken);
    }

    public int addRestaurant(AddRestaurantRequest request, String userToken) {
        int userId = loginService.authenticateToken(userToken);
        Map<String, String> insertValues = new HashMap<>();
        insertValues.put("RestaurantId", Integer.toString(userId));
        insertValues.put("Name", request.getName());
        insertValues.put("Price", Double.toString(request.getPrice()));
        insertValues.put("PictureUri", request.getPictureUri());

        if (querier.insert(RESTAURANT_TABLE, insertValues)) {
            return userId;
        } else {
            return -1;
        }
    }

    public Collection<Restaurant> getRestaurants(String token) {
        int userId = loginService.authenticateToken(token);

        Collection<Restaurant> restaurants = new ArrayList<Restaurant>();
        List<Map<String, String>> result = querier.query(RESTAURANT_TABLE, RESTAURANT_DATA_COLUMNS);
        if (!result.isEmpty()) {
            for (Map<String, String> entry : result) {
                Restaurant mappedRestaurant = RestaurantService.translateRestaurant(entry);
                if (mappedRestaurant != null) {
                    restaurants.add(mappedRestaurant);
                }
            }
        }
        return restaurants;
    }

    private static Restaurant translateRestaurant(Map<String, String> entry) {
        boolean canTranslate = true;
        for (String column : RESTAURANT_DATA_COLUMNS) {
            canTranslate = canTranslate && entry.containsKey(column);
        }
        return !canTranslate ? null : new Restaurant(
                Integer.parseInt(entry.get("RestaurantId")),
                entry.get("Name"),
                Double.parseDouble(entry.get("Price")),
                entry.get("PictureUri")
        );
    }
}
