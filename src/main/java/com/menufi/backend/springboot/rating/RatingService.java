package com.menufi.backend.springboot.rating;

import com.menufi.backend.springboot.sql.Querier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

    @Autowired
    private Querier querier;

    public int putMenuItemRating(int menuItemId, String token, double rating) {
        return -1;
    }

    public MenuItemRating getMenuItemRating(int menuItemId, String token) {
        return null;
    }
}
