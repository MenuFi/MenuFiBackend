package com.menufi.backend.springboot.rating;

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
import java.util.Map;

@RestController
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @CrossOrigin
    @RequestMapping(method=RequestMethod.PUT, value="/items/{menuItemId}/rating/0", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponse<Boolean>> putMenuItemRating(@PathVariable int menuItemId, @RequestBody Map<String, String> body, @RequestHeader("Authorization") String auth) {
        // Expect the format to be: MenuFi mytokenstring
        String userToken = RestUtil.parseAuthHeader(auth);
        if (userToken != null) {
            String ratingRaw = body.get("rating");

            if (ratingRaw != null) {
                try {
                    double rating = Double.parseDouble(ratingRaw);
                    if (!ratingService.putMenuItemRating(menuItemId, userToken, rating)) {
                        return new ResponseEntity<>(new ErrorResponse<>(false, "Failed to create new rating"), HttpStatus.BAD_REQUEST);
                    }
                    return new ResponseEntity<>(
                            new CustomResponse<>("success", true, "Succeeded in putting rating."),
                            HttpStatus.CREATED
                    );
                } catch (NumberFormatException e) {
                    return new ResponseEntity(new ErrorResponse(e), HttpStatus.BAD_REQUEST);
                } catch (InvalidCredentialsException e) {
                    return new ResponseEntity(new ErrorResponse(e), HttpStatus.UNAUTHORIZED);
                }
            }
            return new ResponseEntity<>(new ErrorResponse<>(false, "Missing rating."), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ErrorResponse<>(false, "Improperly formatted token."), HttpStatus.UNAUTHORIZED);
    }

    @CrossOrigin
    @RequestMapping(method=RequestMethod.GET, value="/items/{menuItemId}/rating/0", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponse<MenuItemRating>> getMenuItemRating(@PathVariable int menuItemId, @RequestHeader("Authorization") String auth) {
        // Expect the format to be: MenuFi mytokenstring
        String userToken = RestUtil.parseAuthHeader(auth);
        if (userToken != null) {
            try {
                MenuItemRating menuItemRating = ratingService.getMenuItemRating(menuItemId, userToken);
                if (menuItemRating != null) {
                    return new ResponseEntity<>(new SuccessResponse<>(menuItemRating), HttpStatus.OK);
                }
                return new ResponseEntity<>(new ErrorResponse<>(null, "Could not find the rating for the given user."), HttpStatus.BAD_REQUEST);
            } catch (InvalidCredentialsException e) {
                return new ResponseEntity(new ErrorResponse(e), HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>(new ErrorResponse<>(null, "Improperly formatted token."), HttpStatus.UNAUTHORIZED);
    }

    @CrossOrigin
    @RequestMapping(method=RequestMethod.GET, value="/items/{menuItemId}/rating", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponse<Double>> getMenuItemRatingAverage(@PathVariable int menuItemId, @RequestHeader("Authorization") String auth) {
        String userToken = RestUtil.parseAuthHeader(auth);
        if (userToken != null) {
            try {
                double averageRating = ratingService.getMenuItemRatingAverage(menuItemId, userToken);
                return new ResponseEntity<>(new SuccessResponse<>(averageRating), HttpStatus.OK);
            } catch (InvalidCredentialsException e) {
                return new ResponseEntity(new ErrorResponse(e), HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity(new ErrorResponse(null, "Improperly formatted token."), HttpStatus.UNAUTHORIZED);
        }

    }

}
