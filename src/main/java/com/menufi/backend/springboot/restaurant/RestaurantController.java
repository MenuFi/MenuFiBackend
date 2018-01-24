package com.menufi.backend.springboot.restaurant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @CrossOrigin
    @RequestMapping("/restaurants/nearby")
    public Collection<Restaurant> nearbyRestaurants(@RequestParam(value="address", required=true) String address) {
        return restaurantService.getNearbyRestaurants(address);
    }
}
