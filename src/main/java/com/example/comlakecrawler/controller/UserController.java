package com.example.comlakecrawler.controller;

import com.example.comlakecrawler.service.authentication.UserServices;
import com.example.comlakecrawler.utils.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    @Autowired
    private UserServices userServices;
    @GetMapping(value = "/login")
    public String gotoLogin(Model model){
        userServices.logout();
        UserAccount userAccount = new UserAccount();
        model.addAttribute("user",userAccount);
        return "login";
    }
    @PostMapping("/register_direct")
    public String createNewAccount(@ModelAttribute("user")UserAccount user){
        userServices.addUser(user);
        return "homepage";
    }
    @PostMapping("/login_check")
    public String checkLoginExits(@ModelAttribute("user")UserAccount user){
        userServices.signIn(user);
        return "homepage";
    }
}
