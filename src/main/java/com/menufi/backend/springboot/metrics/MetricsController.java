package com.menufi.backend.springboot.metrics;

import com.menufi.backend.springboot.CustomResponse;
import com.menufi.backend.springboot.ErrorResponse;
import com.menufi.backend.springboot.RestUtil;
import com.menufi.backend.springboot.SuccessResponse;
import com.menufi.backend.springboot.login.InvalidCredentialsException;
import com.menufi.backend.springboot.menu.DietaryPreference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class MetricsController {

    @Autowired
    private MetricsService metricsService;

    @CrossOrigin
    @RequestMapping(method= RequestMethod.GET, value="/restaurants/{restaurantId}/items/{menuItemId}/clicks", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponse<Collection<DietaryPreference>>> getAllItemClicks(@PathVariable int restaurantId, @PathVariable int menuItemId) {
        Collection<MenuItemClick> menuItemClicks = metricsService.getMenuItemClicks(menuItemId);
        if (menuItemClicks != null) {
            return new ResponseEntity(new SuccessResponse<>(menuItemClicks), HttpStatus.OK);
        }
        return new ResponseEntity(new ErrorResponse(menuItemClicks), HttpStatus.BAD_REQUEST);
    }

    @CrossOrigin
    @RequestMapping(method=RequestMethod.POST, value="/restaurants/{restaurantId}/items/{menuItemId}/clicks", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponse<Integer>> addMenuItem(@PathVariable int restaurantId, @PathVariable int menuItemId, @RequestHeader("Authorization") String auth) {
        String userToken = RestUtil.parseAuthHeader(auth);
        if (userToken != null) {
            try {
                int newClickId = metricsService.addMenuItemClick(menuItemId, userToken);
                if (newClickId < 0) {
                    String errorMsg = "Failed to create the menu item click";
                    if (newClickId == -2) {
                        errorMsg = "Click's menu item id did not match the menu item id in the resource identifier";
                    }
                    return new ResponseEntity<>(new CustomResponse<>("error", newClickId, errorMsg), HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>(
                        new CustomResponse<>("success", newClickId, "Succeeded in creating menu item click."),
                        HttpStatus.CREATED
                );
            } catch (InvalidCredentialsException e) {
                return new ResponseEntity<>(new ErrorResponse<>(e), HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>(new ErrorResponse<>(null, "Improperly formatted token"), HttpStatus.UNAUTHORIZED);
        }
    }
}
