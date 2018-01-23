package com.menufi.backend.springboot.restaurant;

import java.util.Collection;

public interface RestaurantService {
    Collection<Restaurant> getNearbyRestaurants(String location);
}
