package org.akon.userapp.security.jwt;

public class JwtResponse {

    private String username;
    public JwtResponse(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}