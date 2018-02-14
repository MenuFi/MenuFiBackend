package com.menufi.backend.springboot.menu;

import com.menufi.backend.springboot.CustomResponse;
import com.menufi.backend.springboot.ErrorResponse;
import com.menufi.backend.springboot.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
public class MenuController {

    @Autowired
    private MenuService menuService;

    @CrossOrigin
    @RequestMapping(method= RequestMethod.GET, value="/preferences", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponse<Collection<DietaryPreference>>> getAllDietaryPreferences() {
        return new ResponseEntity<>(new SuccessResponse<>(menuService.getAllDietaryPreferences()), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method= RequestMethod.GET, value="/items", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponse<Collection<MenuItem>>> getAllMenuItems(@RequestParam(value="restaurantId", required=true) int restaurantId) {
        return new ResponseEntity<>(new SuccessResponse<>(menuService.getAllMenuItems(restaurantId)), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method= RequestMethod.GET, value="/items/{menuItemId}", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponse<MenuItem>> getMenuItem(@RequestParam(value="restaurantId", required=true) int restaurantId, @PathVariable int menuItemId) {
        return new ResponseEntity<>(new SuccessResponse<>(menuService.getMenuItem(restaurantId, menuItemId)), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method=RequestMethod.POST, value="/items", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponse<Integer>> addMenuItem(@RequestBody AddMenuItemRequest body) {
        int newMenuItemId = menuService.addMenuItem(body);
        if (newMenuItemId == -1) {
            return new ResponseEntity<>(new CustomResponse<>("error", newMenuItemId, "Failed to create the menu item"), HttpStatus.BAD_REQUEST);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/items/" + newMenuItemId);
        ResponseEntity<CustomResponse<Integer>> createdResponse = new ResponseEntity<>(
                new CustomResponse<>("success", newMenuItemId, "Succeeded in creating menu item."),
                headers,
                HttpStatus.CREATED
        );
        return createdResponse;
    }
}
