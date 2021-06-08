package com.example.comlakecrawler.controller;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/sources")
public class LinkResourcesController {
    private List<String>sources;

    public LinkResourcesController() {
        sources = new ArrayList<>();
        sources.add("https://api.github.com/repos/dianping/cat/zipball/master");
        sources.add("https://api.github.com/repos/catchorg/Catch2/zipball/master");
        sources.add("https://api.github.com/repos/CleverRaven/Cataclysm-DDA/zipball/master");
        sources.add("https://www.kaggle.com/api/v1/datasets/download/crawford/cat-dataset");
        sources.add("https://www.kaggle.com/api/v1/datasets/download/tongpython/cat-and-dog");
    }
    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public List<String> getSources() {
        return sources;
    }
    @RequestMapping(value = "/specify/{domain}", method = RequestMethod.GET)
    public List<String> getSpecifyDomain(@PathVariable String domain){
        return sources.stream().filter(x->x.contains(domain)).collect(Collectors.toList());
    }
}
