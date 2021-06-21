package com.example.comlakecrawler.service.downloader.target;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DownloadErrorException;
import com.dropbox.core.v2.files.GetMetadataErrorException;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;
import com.example.comlakecrawler.service.downloader.CrawlerInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DropBoxCrawler {
    private String topic;
    private CrawlerInterface listener;
    private DbxClientV2 client;
    private static final String ACCESS_TOKEN = "FXWa9O4CfLQAAAAAAAAAAQEnQ6Yj7EypQile6xPuuog4LUunLsw2A-USbSGuycXw";
    private ArrayList<String>sources;
    public DropBoxCrawler() {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/crawler_csv").build();
        client = new DbxClientV2(config,ACCESS_TOKEN);
        try {
            FullAccount account = client.users().getCurrentAccount();
            System.out.println(account.getName().getDisplayName());
        } catch (DbxException e) {
            e.printStackTrace();
        }
        sources = new ArrayList<>();
    }

    public void setListener(CrawlerInterface listener) {
        this.listener = listener;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void searchMachine(){
        ListFolderResult result = null;
        try {
            result = client.files().listFolder("");
            while (true) {
                for (Metadata metadata : result.getEntries()) {
//                System.out.println(metadata.getPathLower());
                    if (metadata.getPathLower().contains(topic)){
//                    download(client,metadata.getPathLower().replace("/",""),metadata);
//                        System.out.println(metadata.getPathLower());
                        sources.add("no_link:"+"dbx:"+metadata.getPathLower());
                    }
                }
                if (!result.getHasMore()) {
                    break;
                }
                result = client.files().listFolderContinue(result.getCursor());
            }
        } catch (DbxException e) {
            e.printStackTrace();
        }finally {
            saveLink();
        }
    }
    public void download(String filename, String pathMetadata){
        String path = "D:\\save\\sources";
        File file = new File(path, filename);
        if (!file.exists()){
            file.getParentFile().mkdirs();
        }
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            client.files().download(pathMetadata).download(outputStream);
            System.out.println("METADATA"+  pathMetadata.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DownloadErrorException e) {
            e.printStackTrace();
        } catch (GetMetadataErrorException e) {
            e.printStackTrace();
        } catch (DbxException e) {
            e.printStackTrace();
        }
    }
    private void saveLink(){
        if (listener!=null){
            listener.updateSources(topic,sources);
        }
    }
}
