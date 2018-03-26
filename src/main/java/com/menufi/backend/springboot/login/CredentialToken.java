package com.menufi.backend.springboot.login;

import java.time.LocalDateTime;

public class CredentialToken {
    private int userId;
    private String tokenString;
    private String user;
    private LocalDateTime creationDate;

    public CredentialToken(int userId, String user, String tokenString) {
        this.userId = userId;
        this.user = user;
        this.tokenString = tokenString;
        this.creationDate = LocalDateTime.now();
    }

    public int getUserId() { return userId; }

    public String getTokenString() {
        return tokenString;
    }

    public String getUser() {
        return user;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
}
