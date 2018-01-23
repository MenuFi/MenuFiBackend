package com.menufi.backend.springboot.login;

import com.menufi.backend.sql.Querier;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginService {

    @Autowired
    private Querier querier;

    public String loginUser(String username, String password) {
        throw new UnsupportedOperationException();
    }

    public boolean registerUser(String username, String password) {
        throw new UnsupportedOperationException();
    }
}
