package com.menufi.backend.springboot.login;

public interface LoginService {
    String loginUser(String username, String password);
    boolean registerUser(String username, String password);
}
