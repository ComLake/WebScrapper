package com.example.comlakecrawler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {
    @GetMapping(value = "")
    public String viewHomePage(){
        return "homepage";
    }


}
