package com.menufi.backend.springboot.login;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LoginServiceRestaurantTest {

    private static final String USERNAME = "user_test";
    private static final String PASSWORD = "password_test";
    private static final String PASSWORD_INVALID = "pinvalid";
    private static final String USERNAME_NEW = "user_new";
    private static final String USERNAME_EXISTING = "user_exists";

    @Autowired
    private LoginService loginService;

    @Test(expected = BadCredentialsException.class)
    public void testLoginRestaurantBadUsername() {
        loginService.loginRestaurant(null, PASSWORD);
    }

    @Test(expected = BadCredentialsException.class)
    public void testLoginRestaurantBadPassword() {
        loginService.loginRestaurant(USERNAME, null);
    }

    @Test
    public void testRegisterRestaurantHappy() {
        boolean success = loginService.registerRestaurant(USERNAME_NEW, PASSWORD);
        assertEquals("Registration should succeed for any new user", true, success);
    }

    @Test(expected = BadCredentialsException.class)
    public void testRegisterRestaurantBadUsername() {
        loginService.registerRestaurant(null, PASSWORD);
    }

    @Test(expected = BadCredentialsException.class)
    public void testRegisterRestaurantBadPassword() {
        loginService.registerRestaurant(USERNAME, null);
    }

    @Test(expected = InvalidCredentialsException.class)
    public void testRegisterRestaurantAccountExists() {
        boolean success = loginService.registerRestaurant(USERNAME_EXISTING, PASSWORD);
        assertEquals("First registration should succeed", true, success);
        loginService.registerRestaurant(USERNAME_EXISTING, PASSWORD);
    }

    @Test(expected = InvalidCredentialsException.class)
    public void testLoginRestaurantInvalidUser() {
        loginService.loginRestaurant(USERNAME, PASSWORD);
    }

    @Test(expected = InvalidCredentialsException.class)
    public void testLoginRestaurantInvalidPassword() {
        try {
            loginService.registerRestaurant(USERNAME, PASSWORD);
        } catch (InvalidCredentialsException e) {
        }
        loginService.loginRestaurant(USERNAME, PASSWORD_INVALID);
    }

    @Test
    public void testLoginRestaurantHappy() {
        try {
            loginService.registerRestaurant(USERNAME, PASSWORD);
        } catch (InvalidCredentialsException e) {
        }

        LocalDateTime dateNow = LocalDateTime.now();
        LocalDateTime dateBefore = dateNow.minusSeconds(1);
        LocalDateTime dateAfter = dateNow.plusSeconds(1);
        CredentialToken token = loginService.loginRestaurant(USERNAME, PASSWORD);

        assertEquals("Token username should match", token.getUser(), USERNAME);
        assertEquals("Token creation date should be recently before now + 1s", true, token.getCreationDate().isBefore(dateAfter));
        assertEquals("Token creation date should be recently after now - 1s", true, token.getCreationDate().isAfter(dateBefore));
        assertNotNull("Token is not null", token.getTokenString());
    }
}
