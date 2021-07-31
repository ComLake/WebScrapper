package com.example.comlakecrawler.controller;

import com.example.comlakecrawler.service.authentication.UserServices;
import com.example.comlakecrawler.utils.LinkResources;
import com.example.comlakecrawler.utils.SourcesRegistration;
import com.example.comlakecrawler.utils.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {
    @Autowired
    UserServices userServices;

    @GetMapping(value = "")
    public String viewHomePage(Model model) {
        if (userServices.getCurrentUser()==null){
            UserAccount userAccount = new UserAccount();
            model.addAttribute("user",userAccount);
        }else {
            UserAccount userAccount = userServices.getCurrentUser();
            model.addAttribute("user",userAccount);
        }
        return "homepage";
    }
}
