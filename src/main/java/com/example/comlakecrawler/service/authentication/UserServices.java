package com.example.comlakecrawler.service.authentication;

import com.example.comlakecrawler.utils.UserAccount;

import java.util.List;

public interface UserServices {
    void addUser(UserAccount account);
    UserAccount getCurrentUser();
    UserAccount getUserById(long id);
    void signIn(UserAccount account);
    List<UserAccount>getAllUser();
    void logout();
}
