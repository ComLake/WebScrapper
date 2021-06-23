package com.example.comlakecrawler.service.downloader.target;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.http.HttpRequestor;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.*;
import com.dropbox.core.v2.sharing.*;
import com.dropbox.core.v2.users.FullAccount;
import com.example.comlakecrawler.service.downloader.CrawlerInterface;
import com.sun.istack.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DropBoxCrawler {
    private final static String path = "D:\\save\\sources";
    private String topic;
    private CrawlerInterface listener;
    private DbxClientV2 client;
    private String urlSharingLink;
    private static final String ACCESS_TOKEN = "U8PVVxEuP08AAAAAAAAAAdwJ-Xr57JjGbLqVAX9o92wR9s-RTGrGFu9O9sys1lO-";
    private ArrayList<String> sources;

    public DropBoxCrawler() {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/custom_crawler").build();
        client = new DbxClientV2(config, ACCESS_TOKEN);
        try {
            FullAccount account = client.users().getCurrentAccount();
            System.out.println(account.getName().getDisplayName());
        } catch (DbxException e) {
            e.printStackTrace();
        }
        sources = new ArrayList<>();
    }

    public String getUrlSharingLink() {
        return urlSharingLink;
    }

    public void setUrlSharingLink(String urlSharingLink) {
        this.urlSharingLink = urlSharingLink;
    }

    private String nameOfSharingFolder(String url, DbxClientV2 client) {
        SharedLinkMetadata sharedLinkMetadata = null;
        try {
            sharedLinkMetadata = client.sharing().
                    getSharedLinkMetadata(url);
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return sharedLinkMetadata.getName();
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
    public void checkAndCreateSharedLink(){
        boolean run = true;
        try {
            ListFolderResult result = client.files().listFolder("/"+nameOfSharingFolder(urlSharingLink,client));
            while (run){
                for (Metadata metadata : result.getEntries()){
                    ListSharedLinksResult linksResult = client.sharing().
                            listSharedLinksBuilder().
                            withPath(metadata.getPathLower()).withDirectOnly(true).start();
                    if (linksResult.getLinks().toString().equals("[]")) {
                        createLinkSharingFile(client, metadata.getPathLower());
                    }
                }
                run = false;
            }
        } catch (DbxException e) {
            e.printStackTrace();
        }
    }
    public void searchMachine() {
        System.out.println("DO SEARCH");
        ListFolderResult result = null;
        try {
            result = client.files().listFolder("/" + nameOfSharingFolder(urlSharingLink, client));
            while (true) {
                for (Metadata metadata : result.getEntries()) {
                    if (metadata.getName().contains(topic)) {
                        ListSharedLinksResult listSharedLinksResult = null;
                        try {
                            listSharedLinksResult = client.sharing()
                                    .listSharedLinksBuilder()
                                    .withPath(metadata.getPathLower()).withDirectOnly(true)
                                    .start();
                            String jsonTarget = listSharedLinksResult.getLinks().toString();
                            System.out.println(jsonTarget);
                            JSONArray jArray = new JSONArray(jsonTarget);
                            JSONObject jObject = jArray.getJSONObject(0);
                            sources.add("[{\"url\":\""+jObject.get("url")+"\",\"name\":\""+jObject.get("name")+"\"}]"+":dbx");
                        } catch (DbxException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (!result.getHasMore()) {
                    break;
                }
                result = client.files().listFolderContinue(result.getCursor());
            }
        } catch (DbxException e) {
            e.printStackTrace();
        } finally {
            saveLink();
        }
    }

    public void sharingBuilderResult(DbxClientV2 client, String path) {
        ListSharedLinksResult listSharedLinksResult = null;
        try {
            listSharedLinksResult = client.sharing()
                    .listSharedLinksBuilder()
                    .withPath(path).withDirectOnly(true)
                    .start();
            if (listSharedLinksResult.getLinks().toString().equals("[]")) {
                createLinkSharingFile(client, path);
            }
            System.out.println(listSharedLinksResult.getLinks());
            String jsonTarget = listSharedLinksResult.getLinks().toString();
            System.out.println(jsonTarget);
            JSONArray jArray = new JSONArray(jsonTarget);
            JSONObject jObject = jArray.getJSONObject(0);
            sources.add("[{\"url\":\""+jObject.get("url")+"\",\"name\":\""+jObject.get("name")+"\"}]"+":dbx");
        } catch (DbxException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void download(String filename, String pathMetadata) {
        String path = "D:\\save\\sources";
        File file = new File(path, filename);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            client.files().download(pathMetadata).download(outputStream);
            System.out.println("METADATA" + pathMetadata.toString());
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

    public void createLinkSharingFile(DbxClientV2 client, String pathLower) {
        SharedLinkMetadata sharedLinkMetadata = null;
        try {
            sharedLinkMetadata = client.sharing().createSharedLinkWithSettings(pathLower);
        } catch (CreateSharedLinkWithSettingsErrorException ex) {
            System.out.println(ex);
        } catch (DbxException ex) {
            System.out.println(ex);
        }
        System.out.println(sharedLinkMetadata.getUrl());
    }

    public static List<HttpRequestor.Header> addAuthHeader(@Nullable List<HttpRequestor.Header> headers, String accessToken) {
        if (accessToken == null) throw new NullPointerException("accessToken");
        if (headers == null) headers = new ArrayList<HttpRequestor.Header>();
        headers.add(new HttpRequestor.Header("Authorization", "Bearer " + accessToken));
        return headers;
    }

    private void saveLink() {
        if (listener != null) {
            listener.updateSources(topic, sources);
        }
    }

    private void builderConfigurationForSharingMeta(DbxClientV2 client, String url) {
        client.sharing().getSharedLinkFileBuilder(url);
        client.sharing().getSharedLinkMetadataBuilder(url);
    }

    public void downloadSharingFileWithURl(String url) {
        try {
            SharedLinkMetadata sharedLinkMetadata = client.sharing().
                    getSharedLinkMetadata(url);
            String name = sharedLinkMetadata.getName();
            File file = new File(path, name);
            FileOutputStream outputStream = new FileOutputStream(file);
            client.sharing().getSharedLinkFile(url).download(outputStream);
        } catch (DbxException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listSharedFolder(DbxClientV2 client) {
        try {
            ListFoldersResult result = client.sharing().listFolders();
            for (SharedFolderMetadata sharedFolderMeta : result.getEntries()) {
                System.out.println(sharedFolderMeta.getName());
            }
        } catch (DbxException e) {
            e.printStackTrace();
        }
    }
}
