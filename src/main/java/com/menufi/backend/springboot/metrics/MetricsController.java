package com.menufi.backend.springboot.metrics;

import com.menufi.backend.springboot.CustomResponse;
import com.menufi.backend.springboot.ErrorResponse;
import com.menufi.backend.springboot.SuccessResponse;
import com.menufi.backend.springboot.menu.DietaryPreference;
import com.menufi.backend.springboot.menu.MenuService;
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
}
