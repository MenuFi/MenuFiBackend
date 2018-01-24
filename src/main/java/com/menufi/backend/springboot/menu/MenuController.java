package com.menufi.backend.springboot.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class MenuController {

    @Autowired
    private MenuService menuService;

    @CrossOrigin
    @RequestMapping(method= RequestMethod.GET, value="/items")
    public Collection<MenuItem> menuItems(@RequestParam(value="restaurantId", required=true, defaultValue="-1") int restaurantId) {
        return menuService.getMenuItems(restaurantId);
    }

    @CrossOrigin
    @RequestMapping(method=RequestMethod.POST, value="/items")
    public ResponseEntity<?> addMenuItem(@RequestBody AddMenuItemRequest body) {
        MenuItem newMenuItem = menuService.addMenuItem(body);
        if (newMenuItem == null) {
            return ResponseEntity.badRequest().body(new Object());
        }
        return ResponseEntity.ok(newMenuItem);
    }
}
