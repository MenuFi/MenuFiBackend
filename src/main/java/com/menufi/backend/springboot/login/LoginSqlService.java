package com.menufi.backend.springboot.login;

import org.springframework.stereotype.Service;

@Service
public class LoginSqlService implements LoginService {
    @Override
    public String loginUser(String username, String password) {
        return "some hash";
    }

    @Override
    public boolean registerUser(String username, String password) {
        return false;
    }
}
