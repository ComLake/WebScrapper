package com.example.comlakecrawler.controller;

import com.example.comlakecrawler.utils.EmbeddedFile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/zipTarget")
public class EmbeddedFileController {
    private List<String>zipTarget;

    public EmbeddedFileController() {
        zipTarget = new ArrayList<>();
    }
}
