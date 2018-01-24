package com.menufi.backend.springboot.login;

import java.time.LocalDateTime;

public class CredentialToken {
    private String tokenString;
    private String user;
    private LocalDateTime creationDate;

    public CredentialToken(String user, String tokenString) {
        this.user = user;
        this.tokenString = tokenString;
        this.creationDate = LocalDateTime.now();
    }

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
