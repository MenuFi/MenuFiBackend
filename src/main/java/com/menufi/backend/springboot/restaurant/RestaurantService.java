package com.menufi.backend.springboot.restaurant;

import com.menufi.backend.sql.Querier;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

public class RestaurantService {

    @Autowired
    private Querier querier;

    public Collection<Restaurant> getNearbyRestaurants(String location) {
        throw new UnsupportedOperationException();
    }
}
