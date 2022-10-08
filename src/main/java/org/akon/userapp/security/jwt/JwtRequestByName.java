package org.akon.userapp.security.jwt;

public class JwtRequestByName {

    private String username;
    private String password;


    public String getUsername() {
        return username;
    }

    public void setUsername(String email) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}