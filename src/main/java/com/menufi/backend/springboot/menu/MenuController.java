package com.menufi.backend.springboot.menu;

import com.menufi.backend.springboot.CustomResponse;
import com.menufi.backend.springboot.ErrorResponse;
import com.menufi.backend.springboot.RestUtil;
import com.menufi.backend.springboot.SuccessResponse;
import com.menufi.backend.springboot.login.InvalidCredentialsException;
import org.omg.CORBA.DynAnyPackage.Invalid;
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
    public ResponseEntity<CustomResponse<Collection<DietaryPreference>>> getAllDietaryPreferences(@RequestHeader("Authorization") String auth) {
        String userToken = RestUtil.parseAuthHeader(auth);
        if (userToken != null) {
            try {
                return new ResponseEntity<>(new SuccessResponse<>(menuService.getAllDietaryPreferences(userToken)), HttpStatus.OK);
            } catch (InvalidCredentialsException e) {
                return new ResponseEntity<>(new ErrorResponse<>(e), HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>(new ErrorResponse<>(null, "Improperly formatted token."), HttpStatus.UNAUTHORIZED);
        }

    }

    @CrossOrigin
    @RequestMapping(method= RequestMethod.GET, value="/restaurants/{restaurantId}/items", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponse<Collection<MenuItem>>> getAllMenuItems(@PathVariable int restaurantId, @RequestHeader("Authorization") String auth) {
        String userToken = RestUtil.parseAuthHeader(auth);
        if (userToken != null) {
            try {
                return new ResponseEntity<>(new SuccessResponse<>(menuService.getAllMenuItems(restaurantId, userToken)), HttpStatus.OK);
            } catch (InvalidCredentialsException e) {
                return new ResponseEntity<>(new ErrorResponse<>(e), HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>(new ErrorResponse<>(null, "Improperly formatted token."), HttpStatus.UNAUTHORIZED);
        }
    }

    @CrossOrigin
    @RequestMapping(method= RequestMethod.GET, value="/restaurants/{restaurantId}/items/{menuItemId}", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponse<MenuItem>> getMenuItem(@PathVariable int restaurantId, @PathVariable int menuItemId, @RequestHeader("Authorization") String auth) {
        String userToken = RestUtil.parseAuthHeader(auth);
        if (userToken != null) {
            try {
                MenuItem menuItem = menuService.getMenuItem(restaurantId, menuItemId, userToken);
                if (menuItem != null) {
                    return new ResponseEntity<>(new SuccessResponse<>(menuItem), HttpStatus.OK);
                }
                return new ResponseEntity<>(new ErrorResponse<>(menuItem), HttpStatus.NOT_FOUND);
            } catch (InvalidCredentialsException e) {
                return new ResponseEntity(new ErrorResponse(e), HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity(new ErrorResponse<>(false, "Improperly formatted token."), HttpStatus.UNAUTHORIZED);

    }

    @CrossOrigin
    @RequestMapping(method= RequestMethod.PUT, value="/restaurants/{restaurantId}/items/{menuItemId}", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponse<Boolean>> updateMenuItem(@PathVariable int restaurantId, @PathVariable int menuItemId, @RequestBody MenuItem body, @RequestHeader("Authorization") String auth) {
        String userToken = RestUtil.parseAuthHeader(auth);

        if (userToken != null) {
            try {
                if (menuService.updateMenuItem(restaurantId, menuItemId, userToken, body)) {
                    return new ResponseEntity<>(new SuccessResponse<>(true), HttpStatus.OK);
                }
                return new ResponseEntity<>(new ErrorResponse<>(false), HttpStatus.BAD_REQUEST);
            } catch (InvalidCredentialsException e) {
                return new ResponseEntity(new ErrorResponse(e), HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity(new ErrorResponse<>(false, "Improperly formatted token. "), HttpStatus.UNAUTHORIZED);


    }

    @CrossOrigin
    @RequestMapping(method=RequestMethod.POST, value="/restaurants/{restaurantId}/items", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponse<Integer>> addMenuItem(@PathVariable int restaurantId, @RequestBody AddMenuItemRequest body, @RequestHeader("Authorization") String auth) {
        String userToken = RestUtil.parseAuthHeader(auth);
        if (userToken != null) {
            try {
                body.setRestaurantId(restaurantId);
                int newMenuItemId = menuService.addMenuItem(body, userToken);
                if (newMenuItemId == -1) {
                    return new ResponseEntity<>(new CustomResponse<>("error", newMenuItemId, "Failed to create the menu item"), HttpStatus.BAD_REQUEST);
                }
                HttpHeaders headers = new HttpHeaders();
                headers.add("Location", "/restaurants/" + restaurantId + "items/" + newMenuItemId);
                ResponseEntity<CustomResponse<Integer>> createdResponse = new ResponseEntity<>(
                        new CustomResponse<>("success", newMenuItemId, "Succeeded in creating menu item."),
                        headers,
                        HttpStatus.CREATED
                );
                return createdResponse;
            } catch (InvalidCredentialsException e) {
                return new ResponseEntity(new ErrorResponse(e), HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity(new ErrorResponse<>(false, "Improperly formatted token."), HttpStatus.UNAUTHORIZED);
    }
}
