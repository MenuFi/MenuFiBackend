package com.menufi.backend.springboot.restaurant;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;

@Service
public class RestaurantMockService implements RestaurantService {

    Collection<Restaurant> restaurants;

    public RestaurantMockService() {
        restaurants = Arrays.asList(
                new Restaurant(0, "Six Guys", new String[] { "inet_url" }, "Middle of Nowhere", Restaurant.FoodType.AMERICAN),
                new Restaurant(1, "Charlie's Dogs", new String[] { "inet_url" }, "777 Techwood Dr", Restaurant.FoodType.FAST_FOOD),
                new Restaurant(2, "IHIP", new String[] { "inet_url" }, "Ponce", Restaurant.FoodType.DESSERT)
        );
    }

    @Override
    public Collection<Restaurant> getNearbyRestaurants(String location) {
        return restaurants;
    }
}
