package com.example.comlakecrawler.utils;

import java.util.ArrayList;
import java.util.List;

public class UserAccount {
    private String username;
    private String email;
    private List<String> roles;
    private String tokenExpiry;
    private String accessToken;
    private String tokenType;
    private String password;

    public UserAccount() { }

    public UserAccount(String username, String email, List<String> roles, String tokenExpiry, String accessToken, String tokenType, String password) {
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.tokenExpiry = tokenExpiry;
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getTokenExpiry() {
        return tokenExpiry;
    }

    public void setTokenExpiry(String tokenExpiry) {
        this.tokenExpiry = tokenExpiry;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
