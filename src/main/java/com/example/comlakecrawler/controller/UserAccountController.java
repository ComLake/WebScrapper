package com.example.comlakecrawler.controller;

import com.example.comlakecrawler.utils.UserAccount;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserAccountController {
    private List<UserAccount>userAccounts;
    public UserAccountController() {
        userAccounts = new ArrayList<>();
        List<String>roles = new ArrayList<>();
//        roles.add("ROLE_USER");
//        userAccounts.add(new UserAccount("thiet",
//                "thietna22@gmail.com",
//                roles,
//                "",
//                "",
//                "",
//                "1234567"
//        ));
    }
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public List<UserAccount> getUserAccount() {
        return userAccounts;
    }
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public List<UserAccount> create(@RequestBody UserAccount userAccount){
        userAccounts.add(userAccount);
        return userAccounts;
    }
}
