package com.menufi.backend.springboot.login;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.menufi.backend.springboot.sql.Querier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoginService {

    private static final String PATRON_TABLE = "patron_login";
    private static final List<String> PATRON_LOGIN_COLUMNS = ImmutableList.of("PasswordHash");

    private static final List<String> PATRON_REGISTER_COLUMNS = ImmutableList.of("Email");

    private static final String RESTAURANT_TABLE = "restaurant_login";
    private static final List<String> RESTAURANT_LOGIN_COLUMNS = PATRON_LOGIN_COLUMNS;
    private static final List<String> RESTAURANT_REGISTER_COLUMNS = PATRON_REGISTER_COLUMNS;

    private Map<String, CredentialToken> tokenBank;

    @Autowired
    private Querier querier;

    public LoginService() {
        this.tokenBank = new HashMap<>();
    }

    public CredentialToken loginPatron(String email, String password) throws BadCredentialsException, InvalidCredentialsException {
        if (loginUser(email, password, PATRON_TABLE, PATRON_LOGIN_COLUMNS)) {
            CredentialToken token = generateToken(email);
            tokenBank.put(email, token);
            return token;
        }
        throw new InvalidCredentialsException("Wrong email or password.");
    }

    public boolean registerPatron(String email, String password) throws BadCredentialsException, InvalidCredentialsException {
        return registerUser(email, password, PATRON_TABLE, PATRON_REGISTER_COLUMNS);
    }

    public CredentialToken loginRestaurant(String email, String password) throws BadCredentialsException, InvalidCredentialsException {
        if (loginUser(email, password, RESTAURANT_TABLE, RESTAURANT_LOGIN_COLUMNS)) {
            CredentialToken token = generateToken(email);
            tokenBank.put(email, token);
            return token;
        }
        throw new InvalidCredentialsException("Wrong email or password.");
    }

    public boolean registerRestaurant(String email, String password) throws BadCredentialsException, InvalidCredentialsException {
        return registerUser(email, password, RESTAURANT_TABLE, RESTAURANT_REGISTER_COLUMNS);
    }

    public String authenticateToken(String tokenString) {
        CredentialToken token = tokenBank.getOrDefault(tokenString, null);
        if (token == null) {
            throw new InvalidCredentialsException("Bad token.");
        }
        if (token.getCreationDate().plusHours(1).isBefore(LocalDateTime.now())) {
            tokenBank.remove(tokenString);
            throw new InvalidCredentialsException("Token has expired.");
        }
        return token.getUser();
    }

    private CredentialToken generateToken(String user) {
        // TODO: Actually generate tokens...
        return new CredentialToken(user, "Some Expiring Token!");
    }

    private boolean verifyCredentials(String email, String password) {
        return email != null && password != null && !email.isEmpty() && !password.isEmpty();
    }

    private boolean loginUser(String email, String password, String table, List<String> columns) {

        if (!verifyCredentials(email, password)) {
            throw new BadCredentialsException("Incorrectly formatted email or password.");
        }

        Map<String, String> where = ImmutableMap.of("Email", email);
        List<Map<String, String>> result = querier.queryWhere(table, columns, where);

        if (!result.isEmpty()) {
            String resultPasswordHash = result.get(0).getOrDefault("PasswordHash", null);
            return resultPasswordHash.equals(Long.toString(hashPassword(password)));
        }
        return false;
    }

    private boolean registerUser(String email, String password, String table, List<String> columns) {

        if (!verifyCredentials(email, password)) {
            throw new BadCredentialsException("Incorrectly formatted email or password.");
        }

        Map<String, String> where = ImmutableMap.of("Email", email);
        List<Map<String, String>> result = querier.queryWhere(table, columns, where);

        if (!result.isEmpty()) {
            throw new InvalidCredentialsException("Account already exists!");
        }

        Map<String, String> insertValues = new HashMap<>();
        insertValues.put("Email", email);
        insertValues.put("PasswordHash", Long.toString(hashPassword(password)));

        return querier.insert(table, insertValues);
    }

    private long hashPassword(String password) {
        // TODO: Actually hash the password properly
        return password.length();
    }
}
