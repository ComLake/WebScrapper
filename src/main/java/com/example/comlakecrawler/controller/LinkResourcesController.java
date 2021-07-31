package com.example.comlakecrawler.controller;

import com.example.comlakecrawler.service.authentication.UserServices;
import com.example.comlakecrawler.service.downloader.SourcesService;
import com.example.comlakecrawler.utils.LinkResources;
import com.example.comlakecrawler.utils.SourcesRegistration;
import com.example.comlakecrawler.utils.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LinkResourcesController {

    @Autowired
    private SourcesService sourcesService;

    @Autowired
    private UserServices userServices;

    @GetMapping("/result")
    public String presentingListResult(Model model) {
        int currentPage = 1;
        Page<LinkResources> page = sourcesService.getAllResources(currentPage);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("listSources", page.getContent());
        if (userServices.getCurrentUser()==null){
            UserAccount userAccount = new UserAccount();
            model.addAttribute("user",userAccount);
        }else {
            UserAccount userAccount = userServices.getCurrentUser();
            model.addAttribute("user",userAccount);
        }
        return "storage";
    }

    @GetMapping("/result/{pageNumber}")
    public String listByPage(Model model, @PathVariable("pageNumber") int currentPage) {
        Page<LinkResources> page = sourcesService.getAllResources(currentPage);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("listSources", page.getContent());
        if (userServices.getCurrentUser()==null){
            UserAccount userAccount = new UserAccount();
            model.addAttribute("user",userAccount);
        }else {
            UserAccount userAccount = userServices.getCurrentUser();
            model.addAttribute("user",userAccount);
        }
        return "storage";
    }

    @GetMapping(value = "/search")
    public String showCrawlerCenter(Model model) {
        SourcesRegistration sourcesRegister = new SourcesRegistration();
        model.addAttribute("sourcesRegister", sourcesRegister);
        if (userServices.getCurrentUser()==null){
            UserAccount userAccount = new UserAccount();
            model.addAttribute("user",userAccount);
        }else {
            UserAccount userAccount = userServices.getCurrentUser();
            model.addAttribute("user",userAccount);
        }
        return "search";
    }

    @PostMapping("/search_request")
    public String getSearchResult(@ModelAttribute("sourcesRegister") SourcesRegistration sourcesRegister) {
        sourcesService.addSomeSourcesLink(sourcesRegister);
        return "redirect:/result";
    }

    @GetMapping("/showSourcesForUpdate/{id}")
    public String showSourcesForUpdate(@PathVariable(value = "id") long id) {
        sourcesService.getLinksById(id);
        sourcesService.downloadSources(id);
        return "redirect:/result";
    }
    @GetMapping("/results")
    public String sortAllWithSearch(Model model, @Param("keyword") String keyword) {
        int currentPage = 1;
        Page<LinkResources> page = sourcesService.findByName(keyword, currentPage);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("listSources", page.getContent());
        model.addAttribute("keyword", keyword);
        if (userServices.getCurrentUser()==null){
            UserAccount userAccount = new UserAccount();
            model.addAttribute("user",userAccount);
        }else {
            UserAccount userAccount = userServices.getCurrentUser();
            model.addAttribute("user",userAccount);
        }
        return "searching_storage";
    }
    @GetMapping("/results/pages")
    public String sortAllWithSearchNumber(Model model, @Param("keyword") String keyword, @RequestParam int currentPage) {
        Page<LinkResources> page = sourcesService.findByName(keyword, currentPage);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("listSources", page.getContent());
        model.addAttribute("keyword", keyword);
        if (userServices.getCurrentUser()==null){
            UserAccount userAccount = new UserAccount();
            model.addAttribute("user",userAccount);
        }else {
            UserAccount userAccount = userServices.getCurrentUser();
            model.addAttribute("user",userAccount);
        }
        return "searching_storage";
    }

}
