package com.example.comlakecrawler.service.downloader.target;

import com.box.sdk.*;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.example.comlakecrawler.service.downloader.CrawlerInterface;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.*;
import org.jose4j.lang.JoseException;
import org.json.JSONObject;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BoxCrawler {
    private CrawlerInterface listener;
    private static final String USER_ID = "16373928354";
    private static final int MAX_DEPTH = 1;
    private static final int MAX_CACHE_ENTRIES = 100;
    private static BoxDeveloperEditionAPIConnection api;
    private String topic;
    private ArrayList<String>sources;

    public BoxCrawler() {
        // Turn off logging to prevent polluting the output.
        Logger.getLogger("com.box.sdk").setLevel(Level.SEVERE);
        //It is a best practice to use an access token cache to prevent unneeded requests to Box for access tokens.
        //For production applications it is recommended to use a distributed cache like Memcached or Redis, and to
        //implement IAccessTokenCache to store and retrieve access tokens appropriately for your environment.
        IAccessTokenCache accessTokenCache = new InMemoryLRUAccessTokenCache(MAX_CACHE_ENTRIES);
        try {
            Reader reader = new FileReader("src/main/resources/static/box_com/config.json");
            BoxConfig config = BoxConfig.readFrom(reader);
            api = BoxDeveloperEditionAPIConnection.getAppUserConnection(USER_ID,
                    config,
                    accessTokenCache);
            BoxUser.Info userInfo = BoxUser.getCurrentUser(api).getInfo();
            System.out.format("Welcome, %s!\n\n",userInfo.getName());
//            BoxSearch boxSearch = new BoxSearch(api);
//            searchForTopic(boxSearch,topic);
            sources = new ArrayList<>();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setListener(CrawlerInterface listener) {
        this.listener = listener;
    }

    public void searchDemo(){
        Reader reader = null;
        try {
            reader = new FileReader("src/main/resources/static/box_com/config.json");
            BoxConfig config = BoxConfig.readFrom(reader);
            BoxDeveloperEditionAPIConnection api = BoxDeveloperEditionAPIConnection.getAppEnterpriseConnection(config);
            BoxUser.Info userInfo = BoxUser.getCurrentUser(api).getInfo();
            System.out.format("Welcome, %s!\n\n",userInfo.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchForTopic() {
        /**
         * Use this class to specify all the different search parameters that you may want to use.
         * Examples include: type, contentType, folderId's, metadata filters, created, updated.
         * Also shows how to incrementally crawl result sets.
         */
        BoxSearch boxSearch = new BoxSearch(api);
        BoxSearchParameters boxSearchParam = new BoxSearchParameters();
        //searchByDescription(boxSearchParam,boxSearch,topic);
        ownerIdFilterExample(boxSearchParam,boxSearch);
    }
    private void crawlerSearchResult(BoxSearchParameters boxSearchParam,BoxSearch boxSearch){
        //Set up result Partial Object
        PartialCollection<BoxItem.Info>searchResult;
        //Starting point of the result set
        long offset = 0;
        //Number of results that would be pulled back
        long limit = 1000;
        //Storing the full size of the results
        long fullSizeOfResult = 0;

        while (offset <= fullSizeOfResult){
            searchResult = boxSearch.searchRange(offset,limit,boxSearchParam);
            fullSizeOfResult = searchResult.fullSize();
            System.out.println("offset: "+offset+ " of fullSizeOfResult: "+fullSizeOfResult);
            printSearchResults(searchResult);
            offset+=limit;
        }
    }
    private void printSearchResults(PartialCollection<BoxItem.Info>searchResult){
        //Crawl the folder
        System.out.println("--=Results fullResultSize: "+ searchResult.fullSize() + "==--");
        for (BoxItem.Info info:searchResult) {
//            System.out.println("File Found: "+info.getID()+" : "+info.getName()+", Owner: "+info.getOwnedBy().getID());
            sources.add("boxnet:box:"+info.getID()+":"+info.getName()+":"+info.getOwnedBy().getID()+":"+info.getOwnedBy().getName() );
        }
        saveLink();
    }

    public void downloadFile(String id){
        BoxFile file = new BoxFile(api, id);
        BoxFile.Info info = file.getInfo();

        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(new File("D:\\save\\sources\\"+info.getName()+info.getType().replace("file","")));
            file.download(stream, new ProgressListener() {
                public void onProgressChanged(long numBytes, long totalBytes) {
                    double percentComplete = numBytes / totalBytes;
                    System.out.println(percentComplete);
                }
            });
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void searchByDescription(BoxSearchParameters boxSearchParam, BoxSearch boxSearch){
        System.out.println("************Search by Description************");
        List<String>contentTypes = new ArrayList<>();
        contentTypes.add("");
        boxSearchParam.clearParameters();
        boxSearchParam.setContentTypes(contentTypes);
        boxSearchParam.setQuery(topic);
        crawlerSearchResult(boxSearchParam,boxSearch);
    }
    private void ownerIdFilterExample(BoxSearchParameters boxSearchParam, BoxSearch boxSearch){
        System.out.println("***********Owner Id's Filter Search***********");
        List<String>ownerUserIds = new ArrayList<>();
        ownerUserIds.add(USER_ID);
        boxSearchParam.clearParameters();
        boxSearchParam.setQuery(topic);
        boxSearchParam.setOwnerUserIds(ownerUserIds);
        crawlerSearchResult(boxSearchParam,boxSearch);
    }
    private void saveLink(){
        if (listener!=null){
            listener.updateSources(topic,sources);
        }
    }
}
