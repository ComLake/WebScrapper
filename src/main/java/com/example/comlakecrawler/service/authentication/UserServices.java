package com.example.comlakecrawler.service.authentication;

import com.example.comlakecrawler.utils.UserAccount;

public interface UserServices {
    void addUser(UserAccount account);
    UserAccount getCurrentUser();
    void signIn(UserAccount account);
    void logout();
}
