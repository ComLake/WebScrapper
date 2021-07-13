package com.example.comlakecrawler.controller;

import com.example.comlakecrawler.repository.SourcesRepository;
import com.example.comlakecrawler.service.downloader.SourcesService;
import com.example.comlakecrawler.utils.LinkResources;
import com.example.comlakecrawler.utils.SourcesRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        sourcesService.addSomeSourcesLink(sourcesRegister);
        return "redirect:/result";
    }
    @GetMapping("/showSourcesForUpdate/{id}")
    public String showSourcesForUpdate(@PathVariable (value = "id")long id){
        sourcesService.getLinksById(id);
        sourcesService.downloadSources(id);
        return "redirect:/result";
    }
//    @RestController
//    public class SourcesRestSearchController {
        @GetMapping("/results")
        public String sortAllWithSearch(Model model,@RequestParam Optional<String> name,@RequestParam Optional<Integer>pageNumber){
            List<LinkResources>overallResults = sourcesService.findByName(name.orElse("_"));
            List<ArrayList<LinkResources>>theBigPage = new ArrayList<>();
            int i = 0;
            int count = 0;
            ArrayList<LinkResources>theSmallerOne = new ArrayList<>();
            do {
                if(count<6){
                     count++;
                }else {
                    count = 0;
                    theBigPage.add(theSmallerOne);
                    theSmallerOne = new ArrayList<>();
                }
                theSmallerOne.add(overallResults.get(i));
                i++;
            }while (i<overallResults.size());
            model.addAttribute("listSources",theBigPage.get(pageNumber.orElse(0)));
            return "storage";
        }
//    }

}
