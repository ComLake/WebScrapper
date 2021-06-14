package com.example.comlakecrawler.controller;

import com.example.comlakecrawler.service.downloader.SourcesService;
import com.example.comlakecrawler.utils.LinkResources;
import com.example.comlakecrawler.utils.SourcesRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LinkResourcesController {

    @Autowired
    private SourcesService sourcesService;
    @GetMapping("/result")
    public String presentingListResult(Model model){
        model.addAttribute("listSources",sourcesService.getAllResources());
        return "storage";
    }
    @GetMapping(value = "/search")
    public String showCrawlerCenter(Model model) {
        SourcesRegistration sourcesRegister = new SourcesRegistration();
        model.addAttribute("sourcesRegister",sourcesRegister);
        return "search";
    }
    @PostMapping("/search_request")
    public String getSearchResult(@ModelAttribute ("sourcesRegister")SourcesRegistration sourcesRegister){
//        if (sourcesRegister.isKaggle()){
//            LinkResources sources = new LinkResources();
//            sources.setWebsites("kaggle");
//            sources.setTopic(sourcesRegister.getTopic());
//            sourcesService.addSomeSourcesLink(sources);
//        }
//        if (sourcesRegister.isGithub()){
//            LinkResources sources_1 = new LinkResources();
//            sources_1.setWebsites("github");
//            sources_1.setTopic(sourcesRegister.getTopic());
//            sourcesService.addSomeSourcesLink(sources_1);
//        }
        sourcesService.addSomeSourcesLink(sourcesRegister);
        return "redirect:/result";
    }
}
