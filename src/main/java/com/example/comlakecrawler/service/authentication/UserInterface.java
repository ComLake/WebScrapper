package com.example.comlakecrawler.service.authentication;

public interface UserInterface {
    void infoUpdate(String token, String refreshToken, String username,String email,String roles);
}
