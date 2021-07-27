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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.comlakecrawler.service.config.Annotation.*;

@Service
public class SourcesServiceImpl implements SourcesService, CrawlerInterface {
    private ArrayList<LinkResources> linkResources = new ArrayList<>();
    private final String path = "D:\\save\\sources\\";
    @Autowired
    private SourcesRepository sourcesRepository;

    @Override
    public Page<LinkResources> getAllResources(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber -1 ,10);
        return sourcesRepository.findAll(pageable);
    }

    @Override
    public void addSomeSourcesLink(SourcesRegistration sourcesRegister) {
        String linkSharedDropbox = sourcesRegister.getLinkSharedDbx();
        String linkSharedBox = sourcesRegister.getLinkSharedBox();
        String linkSharedDrive = sourcesRegister.getLinkSharedDrive();
        if (!linkSharedDropbox.equals("")) {
            DropBoxCrawler dropBoxCrawler1 = new DropBoxCrawler();
            dropBoxCrawler1.setUrlSharingLink(linkSharedDropbox);
            dropBoxCrawler1.checkAndCreateSharedLink();
        }
        if (sourcesRegister.isGithub()) {
            GithubCrawler githubSearchEngine = new GithubCrawler();
            githubSearchEngine.setListener(this);
            githubSearchEngine.setKeySeek(sourcesRegister.getTopic());
            githubSearchEngine.scrapper();
        }
        if (sourcesRegister.isKaggle()) {
            KaggleCrawler kaggleSearchEngine = new KaggleCrawler();
            kaggleSearchEngine.setListener(this);
            kaggleSearchEngine.setKeySeek(sourcesRegister.getTopic());
            kaggleSearchEngine.scrapper();
        }
        if (sourcesRegister.isDropbox()) {
            DropBoxCrawler dropBoxCrawler = new DropBoxCrawler();
            dropBoxCrawler.setListener(this);
            dropBoxCrawler.setTopic(sourcesRegister.getTopic());
            dropBoxCrawler.setUrlSharingLink(linkSharedDropbox);
            dropBoxCrawler.searchMachine();
        }
        if (sourcesRegister.isBox()) {
            BoxCrawler boxCrawler = new BoxCrawler();
            boxCrawler.setListener(this);
            boxCrawler.setTopic(sourcesRegister.getTopic());
            boxCrawler.setUrlSharedFile(linkSharedBox);
            boxCrawler.searchFileInURLSharing();
        }
        if (sourcesRegister.isDrive()) {
            DriveCrawler driveCrawler = new DriveCrawler();
            driveCrawler.setListener(this);
            driveCrawler.setKeySeek(sourcesRegister.getTopic());
            driveCrawler.setUrlEmbedded(linkSharedDrive);
            driveCrawler.search();
        }
    }

    @Override
    public LinkResources getLinksById(long id) {
        Optional<LinkResources> optionalLink = sourcesRepository.findById(id);
        LinkResources linkResources = null;
        if (optionalLink.isPresent()) {
            linkResources = optionalLink.get();
        } else {
            throw new RuntimeException(" Link is not found for id :: " + id);
        }
        return linkResources;
    }

    @Override
    public void downloadSources(long id) {
        LinkResources linkResources = getLinksById(id);
        String link = linkResources.getLink();
        if (link.contains("kaggle")) {
            KaggleCrawler kaggleSearchEngine = new KaggleCrawler();
            kaggleSearchEngine.setListener(this);
            kaggleSearchEngine.setKeySeek(linkResources.getTopic());
            StringBuffer linkBased = new StringBuffer();
            StringBuffer downloadPath = new StringBuffer();
            StringBuffer texture = new StringBuffer();
            linkBased.append(KAGGLE_API_BASE_URL);
            downloadPath.append(KAGGLE_ZIP_DOWNLOAD);
            texture.append("/");
            String nameTheZip = link.replaceAll(linkBased.toString(), "");
            nameTheZip = nameTheZip.replaceAll(downloadPath.toString(), "");
            nameTheZip = nameTheZip.replaceAll(texture.toString(), "");
            StringBuffer buffer = new StringBuffer();
            buffer.append(nameTheZip);
            buffer.append(".zip");
            StringBuffer destiny = new StringBuffer();
            destiny.append(path);
            destiny.append(buffer);
            System.out.println("Downloading.. " + buffer + " to " + destiny);
            kaggleSearchEngine.download(link, destiny.toString());
        } else if (linkResources.getLink().contains("github")) {
            GithubCrawler githubSearchEngine = new GithubCrawler();
            githubSearchEngine.setListener(this);
            githubSearchEngine.setKeySeek(linkResources.getTopic());
            StringBuffer linkBased = new StringBuffer();
            StringBuffer downloadPath = new StringBuffer();
            StringBuffer texture = new StringBuffer();
            linkBased.append(GITHUB_API_BASE_URL + GITHUB_REPOS);
            downloadPath.append("/" + GITHUB_ZIP_DOWNLOAD);
            texture.append("/");
            String nameTheZip = link.replaceAll(linkBased.toString(), "");
            nameTheZip = nameTheZip.replaceAll(downloadPath.toString(), "");
            nameTheZip = nameTheZip.replaceAll(texture.toString(), "");
            StringBuffer buffer = new StringBuffer();
            buffer.append(nameTheZip);
            buffer.append(".zip");
            StringBuffer destiny = new StringBuffer();
            destiny.append(path);
            destiny.append(buffer);
            System.out.println("Downloading.. " + buffer + " to " + destiny);
            githubSearchEngine.download(link, destiny.toString());
        } else if (linkResources.getLink().contains("app.box")) {
            BoxCrawler boxCrawler = new BoxCrawler();
            System.out.println("Download public box " + linkResources.getName_dataset());
            boxCrawler.setUrlSharedFile(linkResources.getLink());
            boxCrawler.downloadWithSharedLink(linkResources.getName_dataset());
        } else if (linkResources.getLink().contains("dropbox.com")) {
            DropBoxCrawler dropBoxCrawler = new DropBoxCrawler();
            dropBoxCrawler.downloadSharingFileWithURl(linkResources.getLink());
        } else if (linkResources.getWebsites().equals("google drive")) {
            DriveCrawler driveCrawler = new DriveCrawler();
            String[] arr = linkResources.getLink().split(" ");
            driveCrawler.download(arr[1], linkResources.getName_dataset());
        } else {
            throw new RuntimeException("This website haven't been supported");
        }
        this.sourcesRepository.deleteById(id);
    }

    @Override
    public Page<LinkResources> findByName(String name, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber -1 ,10);
        return sourcesRepository.findByName(name,pageable);
    }

    @Override
    public void storageReport(String file, String link) {
        new OfflineFileHandle().unpacking(file);
    }

    @Override
    public void updateSources(String topic, ArrayList<String> sourcesLink) {
        for (String link : sourcesLink) {
            LinkResources linkResources = new LinkResources();
            if (link.contains("kaggle")) {
                StringBuffer linkBased = new StringBuffer();
                StringBuffer downloadPath = new StringBuffer();
                linkBased.append(KAGGLE_API_BASE_URL);
                downloadPath.append(KAGGLE_ZIP_DOWNLOAD);
                String nameTheZip = link.replaceAll(linkBased.toString(), "");
                nameTheZip = nameTheZip.replaceAll(downloadPath.toString(), "");
                String[] array = nameTheZip.split("/", -1);
                linkResources.setWebsites("kaggle");
                linkResources.setLink(link);
                linkResources.setTopic(topic);
                linkResources.setAuthor(array[0]);
                linkResources.setName_dataset(array[1]);
            } else if (link.contains("github")) {
                StringBuffer linkBased = new StringBuffer();
                StringBuffer downloadPath = new StringBuffer();
                linkBased.append(GITHUB_API_BASE_URL + GITHUB_REPOS);
                downloadPath.append("/" + GITHUB_ZIP_DOWNLOAD);
                String nameTheZip = link.replaceAll(linkBased.toString(), "");
                nameTheZip = nameTheZip.replaceAll(downloadPath.toString(), "");
                String[] array = nameTheZip.split("/", -1);
                linkResources.setWebsites("github");
                linkResources.setLink(link);
                linkResources.setTopic(topic);
                linkResources.setAuthor(array[0]);
                linkResources.setName_dataset(array[1]);
            } else if (link.contains("app.box")) {
                System.out.println(link);
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(link);
                    JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                    linkResources.setWebsites("box");
                    linkResources.setLink(jsonObject.get("url").toString());
                    linkResources.setTopic(topic);
                    linkResources.setAuthor("");
                    linkResources.setName_dataset(jsonObject.get("name").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (link.contains("dbx")) {
                try {
                    JSONArray jsonArray = new JSONArray(link);
                    JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                    linkResources.setWebsites("dropbox");
                    linkResources.setLink(jsonObject.get("url").toString());
                    linkResources.setTopic(topic);
                    linkResources.setAuthor("");
                    linkResources.setName_dataset(jsonObject.get("name").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (link.contains("googledrive")) {
                try {
                    JSONArray jsonArray = new JSONArray(link);
                    JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                    linkResources.setWebsites("google drive");
                    linkResources.setLink(jsonObject.get("url").toString() + " " + jsonObject.get("id"));
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
