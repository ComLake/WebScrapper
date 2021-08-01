package com.example.comlakecrawler.service.downloader.target;

import com.box.sdk.*;
import com.example.comlakecrawler.service.downloader.CrawlerInterface;
import com.example.comlakecrawler.service.downloader.SourcesService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DriveCrawler {
    private static final String path = "D:\\save\\sources\\";
    private static final String APPLICATION_NAME = "CrawlerApp";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "src/main/resources/static/lib/credentials.json";
    private ArrayList<String>sources;
    private String urlEmbedded;
    private CrawlerInterface listener;
    private Drive service;
    private String keySeek;

    public DriveCrawler() {
        sources = new ArrayList<>();
        setUp();
    }

    public void setListener(CrawlerInterface listener) {
        this.listener = listener;
    }

    public String getKeySeek() {
        return keySeek;
    }

    public void setKeySeek(String keySeek) {
        this.keySeek = keySeek;
    }

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = new FileInputStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES).
                setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH))).
                setAccessType("offline").build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
    private void setUp() {
        System.out.println("GOOGLE DRIVE API LAUNCH");
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT)).
                    setApplicationName(APPLICATION_NAME).build();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void list() {
        FileList result = null;
        try {
            result = service.files().list().setPageSize(10).setFields("nextPageToken, files(id, name)").execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
            }
        }
    }

    private List<File> getSubFolderByQuery(String googleFolderIdParent, List<File>list) {
        Drive driveService = service;
        String pageToken = null;
        list = new ArrayList<>();
        String query = null;
        if (googleFolderIdParent == null) {
            query = " mimeType = 'application/vnd.google-apps.folder' "
                    + " and 'root' in parents";
        } else {
            query = " mimeType = 'application/vnd.google-apps.folder' "
                    + " and '" + googleFolderIdParent + "' in parents";
        }
        do {
            try {
                FileList result = driveService.files().list().setQ(query)
                        .setSpaces("drive")
                        .setFields("nextPageToken, files(id, name, createdTime)")
                        .setPageToken(pageToken).execute();
                for (File file : result.getFiles()) {
                    list.add(file);
                }
                pageToken = result.getNextPageToken();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (pageToken != null);
        return list;
    }

    private List<File> getFileByQuery(String googleFolderIdParent,List<File>list) {
        Drive driveService = service;
        String pageToken = null;
        list = new ArrayList<>();
        String query = null;
        if (googleFolderIdParent == null) {
            query = " mimeType != 'application/vnd.google-apps.folder' "
                    + " and 'root' in parents";
        } else{
            query = " mimeType != 'application/vnd.google-apps.folder' "
                    + " and '" + googleFolderIdParent + "' in parents";
        }
        do {
            try {
                FileList result = driveService.files().list().setQ(query)
                        .setSpaces("drive")
                        .setFields("nextPageToken, files(id, name, createdTime)")
                        .setPageToken(pageToken).execute();
                for (File file:result.getFiles()) {
                    list.add(file);
                }
                pageToken = result.getNextPageToken();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }while (pageToken != null);
        return list;
    }
    public void download(String fileId, String fileName,String token){
        try {
            java.io.File file = new java.io.File(path+"ggdrive_"+fileName);
            OutputStream outputStream = new FileOutputStream(file);
            service.files().get(fileId).executeMediaAndDownloadTo(outputStream);
            report(path+"ggdrive_"+fileName,urlEmbedded, token);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Permission createPublicPermission(String idFile) throws IOException {
        String permissionType = "anyone";
        String permissionRole = "reader";
        Permission newPermission = new Permission();
        newPermission.setType(permissionType);
        newPermission.setRole(permissionRole);
        return service.permissions().create(idFile,newPermission).execute();
    }
    public void search(){
        System.out.println("SEARCH");
        String []arrEmbedded = urlEmbedded.split("/",-1);
        String embeddedLink = arrEmbedded[arrEmbedded.length-1];
        List<File>files = new ArrayList<>();
        if (getFileByQuery(embeddedLink,files)==null||getFileByQuery(embeddedLink,files).isEmpty()){
            files = getSubFolderByQuery(embeddedLink,files);
        }else {
            files = getFileByQuery(embeddedLink,files);
        }
        for (File file:files) {
            if (file.getName().contains(keySeek)){
                sources.add("[{\"id\":\"" + file.getId() + "\",\"url\":\""+urlEmbedded+ "\",\"name\":\"" + file.getName() +"\",\"website\":\""+"googledrive" +"\"}]");
            }
        }
        saveLink();
    }
    private void report(String file,String sources,String token) {
        if (listener != null) {
            listener.storageReport(file, sources, token);
        }
    }
    public void setUrlEmbedded(String urlEmbedded) {
        this.urlEmbedded = urlEmbedded;
    }
    private void saveLink(){
        if (listener!=null){
            listener.updateSources(keySeek,sources);
        }
    }
}
