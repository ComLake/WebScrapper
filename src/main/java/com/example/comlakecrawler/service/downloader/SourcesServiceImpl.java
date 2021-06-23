package com.example.comlakecrawler.service.downloader;

import com.dropbox.core.DbxException;
import com.example.comlakecrawler.repository.SourcesRepository;
import com.example.comlakecrawler.service.config.OfflineFileHandle;
import com.example.comlakecrawler.service.downloader.target.*;
import com.example.comlakecrawler.utils.LinkResources;
import com.example.comlakecrawler.utils.SourcesRegistration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.comlakecrawler.service.config.Annotation.*;

@Service
public class SourcesServiceImpl implements SourcesService,CrawlerInterface{
    private ArrayList<LinkResources>linkResources = new ArrayList<>();
    private final String path = "D:\\save\\sources\\";
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
        }
        if (sourcesRegister.isKaggle()){
            KaggleCrawler kaggleSearchEngine = new KaggleCrawler();
            kaggleSearchEngine.setListener(this);
            kaggleSearchEngine.setKeySeek(sourcesRegister.getTopic());
            kaggleSearchEngine.scrapper();
        }
        if (sourcesRegister.isDropbox()){
            DropBoxCrawler dropBoxCrawler = new DropBoxCrawler();
            dropBoxCrawler.setListener(this);
            dropBoxCrawler.setTopic(sourcesRegister.getTopic());
            dropBoxCrawler.setUrlSharingLink(sourcesRegister.getLinkSharedDbx());
            dropBoxCrawler.searchMachine();
//            System.out.println(dropBoxCrawler.getUrlSharingLink());
        }
        if (sourcesRegister.isBox()){
            BoxCrawler boxCrawler = new BoxCrawler();
            boxCrawler.setListener(this);
            boxCrawler.setTopic(sourcesRegister.getTopic());
            boxCrawler.searchForTopic();
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
        LinkResources linkResources = getLinksById(id);
        String link = linkResources.getLink();
        if (link.contains("kaggle")){
            KaggleCrawler kaggleSearchEngine = new KaggleCrawler();
            kaggleSearchEngine.setListener(this);
            kaggleSearchEngine.setKeySeek(linkResources.getTopic());
            StringBuffer linkBased = new StringBuffer();
            StringBuffer downloadPath = new StringBuffer();
            StringBuffer texture = new StringBuffer();
            linkBased.append(KAGGLE_API_BASE_URL);
            downloadPath.append(KAGGLE_ZIP_DOWNLOAD);
            texture.append("/");
            String nameTheZip = link.replaceAll(linkBased.toString(),"");
            nameTheZip = nameTheZip.replaceAll(downloadPath.toString(),"");
            nameTheZip = nameTheZip.replaceAll(texture.toString(),"");
            StringBuffer buffer = new StringBuffer();
            buffer.append(nameTheZip);
            buffer.append(".zip");
            StringBuffer destiny = new StringBuffer();
            destiny.append(path);
            destiny.append(buffer);
            System.out.println("Downloading.. "+buffer + " to "+destiny);
            kaggleSearchEngine.download(link,destiny.toString());
        }else if (linkResources.getLink().contains("github")){
            GithubCrawler githubSearchEngine = new GithubCrawler();
            githubSearchEngine.setListener(this);
            githubSearchEngine.setKeySeek(linkResources.getTopic());
            StringBuffer linkBased = new StringBuffer();
            StringBuffer downloadPath = new StringBuffer();
            StringBuffer texture = new StringBuffer();
            linkBased.append(GITHUB_API_BASE_URL+GITHUB_REPOS);
            downloadPath.append("/"+GITHUB_ZIP_DOWNLOAD);
            texture.append("/");
            String nameTheZip = link.replaceAll(linkBased.toString(),"");
            nameTheZip = nameTheZip.replaceAll(downloadPath.toString(),"");
            nameTheZip = nameTheZip.replaceAll(texture.toString(),"");
            StringBuffer buffer = new StringBuffer();
            buffer.append(nameTheZip);
            buffer.append(".zip");
            StringBuffer destiny = new StringBuffer();
            destiny.append(path);
            destiny.append(buffer);
            System.out.println("Downloading.. "+buffer + " to "+destiny);
            githubSearchEngine.download(link,destiny.toString());
        }else if (linkResources.getLink().contains("boxnet")){
//            BoxCrawler boxCrawler = new BoxCrawler();
//            String linkDownload = linkResources.getLink();
//            String []arraySet = linkDownload.split(":",-1);
//            boxCrawler.downloadFile(arraySet[2]);
        }else if(linkResources.getLink().contains("dropbox.com")){
            DropBoxCrawler dropBoxCrawler = new DropBoxCrawler();
//            String linkDbxDownload = linkResources.getLink();
            dropBoxCrawler.downloadSharingFileWithURl(linkResources.getLink());
        }
        else {
            throw new RuntimeException("This website haven't been supported");
        }
        this.sourcesRepository.deleteById(id);
    }

    @Override
    public void storageReport(String file, String link) {
        new OfflineFileHandle().unpacking(file);
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
            }else if(link.contains("boxnet")){
                String[] arrayBox = link.split(":",-1);
                linkResources.setWebsites(arrayBox[1]);
                linkResources.setLink(link);
                linkResources.setTopic(topic);
                linkResources.setAuthor(arrayBox[5]);
                linkResources.setName_dataset(arrayBox[3]);
            }else if (link.contains("dbx")){
                try {
                    JSONArray jsonArray = new JSONArray(link);
                    JSONObject jsonObject = (JSONObject)jsonArray.get(0);
                    linkResources.setWebsites("dropbox");
                    linkResources.setLink(jsonObject.get("url").toString());
                    linkResources.setTopic(topic);
                    linkResources.setAuthor("");
                    linkResources.setName_dataset(jsonObject.get("name").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            this.sourcesRepository.save(linkResources);
        }
    }
}
