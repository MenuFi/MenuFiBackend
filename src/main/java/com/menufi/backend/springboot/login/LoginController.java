package com.menufi.backend.springboot.login;

import com.menufi.backend.springboot.CustomResponse;
import com.menufi.backend.springboot.ErrorResponse;
import com.menufi.backend.springboot.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @CrossOrigin
    @RequestMapping(method=RequestMethod.POST, value="/patron/loginToken", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponse<String>> loginPatron(@RequestBody Map<String, Object> rawBody) {
        Map<String, String> bodyStringMap = translateRequestBodyToStringMap(rawBody);

        String email = bodyStringMap.getOrDefault("email", null);
        String password = bodyStringMap.getOrDefault("password", null);

        try {
            String tokenString = this.loginService.loginPatron(email, password).getTokenString();
            return new ResponseEntity<>(new SuccessResponse<>(tokenString), HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ErrorResponse<>(e), HttpStatus.BAD_REQUEST);
        } catch (InvalidCredentialsException e) {
            return new ResponseEntity<>(new ErrorResponse<>(e), HttpStatus.UNAUTHORIZED);
        }
    }

    @CrossOrigin
    @RequestMapping(method=RequestMethod.POST, value="/patron/registration", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponse<Boolean>> registerPatron(@RequestBody Map<String, Object> rawBody) {
        Map<String, String> bodyStringMap = translateRequestBodyToStringMap(rawBody);

        String email = bodyStringMap.getOrDefault("email", null);
        String password = bodyStringMap.getOrDefault("password", null);

        try {
            boolean success = this.loginService.registerPatron(email, password);
            return new ResponseEntity<>(new SuccessResponse<>(success), HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ErrorResponse<>(e), HttpStatus.BAD_REQUEST);
        } catch (InvalidCredentialsException e) {
            return new ResponseEntity<>(new ErrorResponse<>(e), HttpStatus.UNAUTHORIZED);
        }
    }

    @CrossOrigin
    @RequestMapping(method=RequestMethod.POST, value="/restaurant/loginToken", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponse<String>> loginRestaurant(@RequestBody Map<String, Object> rawBody) {
        Map<String, String> bodyStringMap = translateRequestBodyToStringMap(rawBody);

        String email = bodyStringMap.getOrDefault("email", null);
        String password = bodyStringMap.getOrDefault("password", null);

        try {
            String tokenString = this.loginService.loginRestaurant(email, password).getTokenString();
            return new ResponseEntity<>(new SuccessResponse<>(tokenString), HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ErrorResponse<>(e), HttpStatus.BAD_REQUEST);
        } catch (InvalidCredentialsException e) {
            return new ResponseEntity<>(new ErrorResponse<>(e), HttpStatus.UNAUTHORIZED);
        }
    }

    @CrossOrigin
    @RequestMapping(method=RequestMethod.POST, value="/restaurant/registration", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponse<Boolean>> registerRestaurant(@RequestBody Map<String, Object> rawBody) {
        Map<String, String> bodyStringMap = translateRequestBodyToStringMap(rawBody);

        String email = bodyStringMap.getOrDefault("email", null);
        String password = bodyStringMap.getOrDefault("password", null);

        try {
            boolean success = this.loginService.registerRestaurant(email, password);
            return new ResponseEntity<>(new SuccessResponse<>(success), HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ErrorResponse<>(e), HttpStatus.BAD_REQUEST);
        } catch (InvalidCredentialsException e) {
            return new ResponseEntity<>(new ErrorResponse<>(e), HttpStatus.UNAUTHORIZED);
        }
    }

    private Map<String, String> translateRequestBodyToStringMap(Map<String, Object> rawBody) {
        Map<String, String> stringMap = new HashMap<>();

        for (String key : rawBody.keySet()) {
            Object obj = rawBody.get(key);
            String val = (obj == null) ? null : obj.toString();
            stringMap.put(key, val);
        }

        return stringMap;
    }

}
