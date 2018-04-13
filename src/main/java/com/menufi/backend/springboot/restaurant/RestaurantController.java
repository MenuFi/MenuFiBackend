package com.menufi.backend.springboot.restaurant;

import com.menufi.backend.springboot.CustomResponse;
import com.menufi.backend.springboot.ErrorResponse;
import com.menufi.backend.springboot.RestUtil;
import com.menufi.backend.springboot.SuccessResponse;
import com.menufi.backend.springboot.login.InvalidCredentialsException;
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
    public ResponseEntity<CustomResponse<Collection<Restaurant>>> allRestaurants(@RequestHeader("Authorization") String auth) {
        String userToken = RestUtil.parseAuthHeader(auth);
        if (userToken != null) {
            try {
                return new ResponseEntity<>(new SuccessResponse<>(restaurantService.getRestaurants(userToken)), HttpStatus.OK);
            } catch (InvalidCredentialsException e) {
                return new ResponseEntity<>(new ErrorResponse<>(e), HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>(new ErrorResponse<>(null, "Improperly formatted token"), HttpStatus.UNAUTHORIZED);
        }
    }
}
