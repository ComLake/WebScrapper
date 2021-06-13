package com.example.comlakecrawler.controller;

import com.example.comlakecrawler.utils.UserAccount;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AppController {
    @GetMapping(value = "")
    public String viewHomePage(){
        return "homepage";
    }
    @GetMapping(value = "/search")
    public String showCrawlerCenter(){ return "search";}
//    @GetMapping(value = "/register")
//    public String showRegisterForm(Model model){
//        UserAccount user = new UserAccount();
//        model.addAttribute("user",user);
//        return "login";}
//    @PostMapping(value = "/register")
//    public String submitRegister(@ModelAttribute("user")UserAccount user){
//        System.out.println(user.getToString());
//        return "homepage";
//    }
}
