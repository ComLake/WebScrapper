package com.example.comlakecrawler.service.authentication;

import com.example.comlakecrawler.utils.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class UserServiceImpl implements UserServices, UserInterface{
    private String username;
    private String email;
    private String token;
    private String refreshToken;
    private String roles;

    public void addUser(UserAccount account) {
        AuthenticateComLake authenticate = new AuthenticateComLake();
        authenticate.setUsername(account.getUsername());
        authenticate.setEmail(account.getEmail());
        authenticate.setPasswords(account.getPassword());
        authenticate.createAccount();
        authenticate.login();
    }

    @Override
    public UserAccount getCurrentUser() {
        UserAccount userAccount = new UserAccount();
        if (username!=null){
            userAccount.setUsername(username);
        }else {
            userAccount.setUsername("");
        }
        if (email!=null){
            userAccount.setEmail(email);
        }else {
            userAccount.setEmail("");
        }
        if (token!=null){
            userAccount.setToken(token);
        }else {
            userAccount.setToken("");
        }
        if (refreshToken!=null){
            userAccount.setRefreshToken(refreshToken);
        }else {
            userAccount.setRefreshToken("");
        }
        if (roles !=null){
            userAccount.setRoles(roles);
        }else {
            userAccount.setRoles("user");
        }
        return userAccount;
    }

    @Override
    public void signIn(UserAccount account) {
        AuthenticateComLake authenticate = new AuthenticateComLake();
        authenticate.setUsername(account.getUsername());
        authenticate.setPasswords(account.getPassword());
        authenticate.login();
    }

    @Override
    public void logout() {
        new AuthenticateComLake().logout();
    }

    @Override
    public void infoUpdate(String token, String refreshToken, String username,String email,String roles) {
        this.username = username;
        this.refreshToken = refreshToken;
        this.token = token;
        this.email = email;
        this.roles = roles;
    }
}
