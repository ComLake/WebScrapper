package com.example.comlakecrawler.controller;

import com.example.comlakecrawler.service.downloader.SourcesService;
import com.example.comlakecrawler.utils.LinkResources;
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
    @PostMapping("/search_request")
    public String getSearchResult(@ModelAttribute ("sources")LinkResources sources){
        sourcesService.addSomeSourcesLink(sources);
        return "redirect:/result";
    }
}
