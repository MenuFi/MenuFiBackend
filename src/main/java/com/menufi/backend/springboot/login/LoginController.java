package com.menufi.backend.springboot.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @CrossOrigin
    @RequestMapping(method=RequestMethod.POST, value="/patron/loginToken")
    public String loginPatron(@RequestBody Map<String, Object> rawBody) {
        Map<String, String> bodyStringMap = translateRequestBodyToStringMap(rawBody);

        String username = bodyStringMap.getOrDefault("username", null);
        String password = bodyStringMap.getOrDefault("password", null);

        return this.loginService.loginPatron(username, password).getTokenString();
    }

    @CrossOrigin
    @RequestMapping(method=RequestMethod.POST, value="/patron/registration")
    public boolean registerPatron(@RequestBody Map<String, Object> rawBody) {
        Map<String, String> bodyStringMap = translateRequestBodyToStringMap(rawBody);

        String username = bodyStringMap.getOrDefault("username", null);
        String password = bodyStringMap.getOrDefault("password", null);

        return this.loginService.registerPatron(username, password);
    }

    @CrossOrigin
    @RequestMapping(method=RequestMethod.POST, value="/restaurant/loginToken")
    public String loginRestaurant(@RequestBody Map<String, Object> rawBody) {
        Map<String, String> bodyStringMap = translateRequestBodyToStringMap(rawBody);

        String username = bodyStringMap.getOrDefault("username", null);
        String password = bodyStringMap.getOrDefault("password", null);

        return this.loginService.loginRestaurant(username, password).getTokenString();
    }

    @CrossOrigin
    @RequestMapping(method=RequestMethod.POST, value="/restaurant/registration")
    public boolean registerRestaurant(@RequestBody Map<String, Object> rawBody) {
        Map<String, String> bodyStringMap = translateRequestBodyToStringMap(rawBody);

        String username = bodyStringMap.getOrDefault("username", null);
        String password = bodyStringMap.getOrDefault("password", null);

        return this.loginService.registerRestaurant(username, password);
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
