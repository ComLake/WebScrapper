package com.example.comlakecrawler.service.authentication;

import com.example.comlakecrawler.repository.UserRepository;
import com.example.comlakecrawler.utils.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserServices, UserInterface{
    private String username;
    private String email;
    private String token;
    private String refreshToken;
    private String roles;

    @Autowired
    private UserRepository userRepository;

    public void addUser(UserAccount account) {
        AuthenticateComLake authenticate = new AuthenticateComLake();
        authenticate.setUsername(account.getUsername());
        authenticate.setPasswords(account.getPassword());
        authenticate.setEmail(account.getEmail());
        authenticate.setListener(this);
        authenticate.logout();
        authenticate.createAccount();
    }

    @Override
    public UserAccount getCurrentUser() {
        List<UserAccount>userAccounts = getAllUser();
        UserAccount userAccount = getUserById(userAccounts.size());
        return userAccount;
    }

    @Override
    public UserAccount getUserById(long id) {
        Optional<UserAccount>user = userRepository.findById(id);
        UserAccount userAccount = null;
        if (user.isPresent()){
            userAccount = user.get();
        }else {
            throw new RuntimeException("User is not found for id :: "+id);
        }
        return userAccount;
    }

    @Override
    public void signIn(UserAccount account) {
        AuthenticateComLake authenticate = new AuthenticateComLake();
        authenticate.setUsername(account.getUsername());
        authenticate.setPasswords(account.getPassword());
        authenticate.setListener(this);
        authenticate.logout();
        authenticate.login();
    }

    @Override
    public List<UserAccount> getAllUser() {
        List<UserAccount> userAccounts = userRepository.findAll();
        return userAccounts;
    }

    @Override
    public void logout() {
        new AuthenticateComLake().logout();
    }

    @Override
    public void infoUpdate(String token, String refreshToken, String username, String email, String roles) {
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(username);
        userAccount.setEmail(email);
        userAccount.setRoles(roles);
        userAccount.setRefreshToken(refreshToken);
        userAccount.setToken(token);
        System.out.println("Before saved");
        System.out.println(userAccount.getEmail()+","+userAccount.getUsername());
        this.userRepository.save(userAccount);
    }
}
