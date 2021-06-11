package com.example.comlakecrawler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {
    @GetMapping(value = "")
    public String viewHomePage(){
        return "homepage";
    }
    @GetMapping(value = "/login")
    public String showLoginForm(){ return "login";}
    @GetMapping(value = "/search")
    public String showCrawlerCenter(){ return "search";}
}
