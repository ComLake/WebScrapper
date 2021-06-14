package com.example.comlakecrawler.service.downloader;

import com.example.comlakecrawler.repository.SourcesRepository;
import com.example.comlakecrawler.service.downloader.target.Crawler;
import com.example.comlakecrawler.service.downloader.target.GithubCrawler;
import com.example.comlakecrawler.service.downloader.target.KaggleCrawler;
import com.example.comlakecrawler.utils.LinkResources;
import com.example.comlakecrawler.utils.SourcesRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.comlakecrawler.service.config.Annotation.*;

@Service
public class SourcesServiceImpl implements SourcesService,CrawlerInterface{
    private ArrayList<LinkResources>linkResources = new ArrayList<>();
    @Autowired
    private SourcesRepository sourcesRepository;
    @Override
    public List<LinkResources> getAllResources() {
        return sourcesRepository.findAll();
    }

    @Override
    public void addSomeSourcesLink(SourcesRegistration sourcesRegister) {
        if (sourcesRegister.isGithub()){
            GithubCrawler githubSearchEngine = new GithubCrawler();
            githubSearchEngine.setListener(this);
            githubSearchEngine.setKeySeek(sourcesRegister.getTopic());
            githubSearchEngine.scrapper();
//            LinkResources linkResources = new LinkResources();
//            linkResources.setTopic(sourcesRegister.getTopic());
//            linkResources.setWebsites("github");
//            this.sourcesRepository.save(linkResources);
        }
        if (sourcesRegister.isKaggle()){
            KaggleCrawler kaggleSearchEngine = new KaggleCrawler();
            kaggleSearchEngine.setListener(this);
            kaggleSearchEngine.setKeySeek(sourcesRegister.getTopic());
            kaggleSearchEngine.scrapper();
//            LinkResources linkResources_1 = new LinkResources();
//            linkResources_1.setTopic(sourcesRegister.getTopic());
//            linkResources_1.setWebsites("kaggle");
//            this.sourcesRepository.save(linkResources_1);
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
    public void updateSources(String topic,ArrayList<String> sourcesLink) {
        for (String link:sourcesLink) {
            LinkResources linkResources = new LinkResources();
            if (link.contains("kaggle")) {
                StringBuffer linkBased = new StringBuffer();
                StringBuffer downloadPath = new StringBuffer();
                linkBased.append(KAGGLE_API_BASE_URL);
                downloadPath.append(KAGGLE_ZIP_DOWNLOAD);
                String nameTheZip = link.replaceAll(linkBased.toString(),"");
                nameTheZip = nameTheZip.replaceAll(downloadPath.toString(),"");
                String[] array = nameTheZip.split("/", -1);
                linkResources.setWebsites("kaggle");
                linkResources.setLink(link);
                linkResources.setTopic(topic);
                linkResources.setAuthor(array[0]);
                linkResources.setName_dataset(array[1]);
            }else if (link.contains("github")){
                StringBuffer linkBased = new StringBuffer();
                StringBuffer downloadPath = new StringBuffer();
                StringBuffer texture = new StringBuffer();
                linkBased.append(GITHUB_API_BASE_URL+GITHUB_REPOS);
                downloadPath.append("/"+GITHUB_ZIP_DOWNLOAD);
                String nameTheZip = link.replaceAll(linkBased.toString(),"");
                nameTheZip = nameTheZip.replaceAll(downloadPath.toString(),"");
                String[] array = nameTheZip.split("/", -1);
                linkResources.setWebsites("github");
                linkResources.setLink(link);
                linkResources.setTopic(topic);
                linkResources.setAuthor(array[0]);
                linkResources.setName_dataset(array[1]);
            }
            this.sourcesRepository.save(linkResources);
        }
    }
}
