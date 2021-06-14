package com.example.comlakecrawler.service.downloader;

import com.example.comlakecrawler.repository.SourcesRepository;
import com.example.comlakecrawler.utils.LinkResources;
import com.example.comlakecrawler.utils.SourcesRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SourcesServiceImpl implements SourcesService,CrawlerInterface{
    @Autowired
    private SourcesRepository sourcesRepository;
    @Override
    public List<LinkResources> getAllResources() {
        return sourcesRepository.findAll();
    }

    @Override
    public void addSomeSourcesLink(SourcesRegistration sourcesRegister) {
        if (sourcesRegister.isGithub()){
            LinkResources linkResources = new LinkResources();
            linkResources.setTopic(sourcesRegister.getTopic());
            linkResources.setWebsites("github");
            this.sourcesRepository.save(linkResources);
        }
        if (sourcesRegister.isKaggle()){
            LinkResources linkResources_1 = new LinkResources();
            linkResources_1.setTopic(sourcesRegister.getTopic());
            linkResources_1.setWebsites("kaggle");
            this.sourcesRepository.save(linkResources_1);
        }
    }

    @Override
    public LinkResources getLinksById(long id) {
        Optional<LinkResources>optionalLink = sourcesRepository.findById(id);
        LinkResources linkResources = null;
        if (optionalLink.isPresent()){
            linkResources = optionalLink.get();
        }else {
            throw new RuntimeException(" Link is not found for id :: "+id);
        }
        return linkResources;
    }

    @Override
    public void downloadSources(long id) {
        this.sourcesRepository.deleteById(id);
    }

    @Override
    public void storageReport(String file, String link) {

    }

    @Override
    public void updateSources(ArrayList<String> sourcesLink) {

    }
}
