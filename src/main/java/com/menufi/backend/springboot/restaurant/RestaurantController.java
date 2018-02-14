package com.menufi.backend.springboot.restaurant;

import com.menufi.backend.springboot.CustomResponse;
import com.menufi.backend.springboot.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @CrossOrigin
    @RequestMapping(method= RequestMethod.GET, value="/restaurants", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponse<Collection<Restaurant>>> allRestaurants() {
        return new ResponseEntity<>(new SuccessResponse<>(restaurantService.getRestaurants()), HttpStatus.OK);
    }
}
