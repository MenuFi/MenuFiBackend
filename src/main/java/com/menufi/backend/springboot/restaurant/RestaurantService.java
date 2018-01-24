package com.menufi.backend.springboot.restaurant;

import com.menufi.backend.springboot.sql.Querier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class RestaurantService {

    @Autowired
    private Querier querier;

    public Collection<Restaurant> getNearbyRestaurants(String location) {
        throw new UnsupportedOperationException();
    }
}
