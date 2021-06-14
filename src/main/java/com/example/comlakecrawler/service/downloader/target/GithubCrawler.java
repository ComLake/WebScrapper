package com.example.comlakecrawler.service.downloader.target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.comlakecrawler.service.config.Annotation.*;

public class GithubCrawler extends Crawler{
    public GithubCrawler() {
        sources = new ArrayList<>();
    }

    @Override
    public void download(String link, String path) {
        onStart();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (!isExit){
                    try {
                        File file = new File(path);
                        if (!file.exists()){
                            file.getParentFile().mkdirs();
                            file.createNewFile();
                            URL url = new URL(link);
                            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                            urlConnection.setRequestMethod("GET");
                            urlConnection.setRequestProperty("User-Agent","Mozilla/5.0");
                            int responseCode = urlConnection.getResponseCode();
                            System.out.println("\nSending 'GET' Request to URL"+url);
                            System.out.println("Response code :"+responseCode);
                            double fileSize = (double) urlConnection.getContentLengthLong();
                            BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                            FileOutputStream fileOS = new FileOutputStream(file);
                            BufferedOutputStream outputStream = new BufferedOutputStream(fileOS,BUFFER_SIZE);
                            byte[]buffer = new byte[BUFFER_SIZE];
                            double downloaded = 0.00;
                            int percentagesDownload;
                            int read;
                            while ((read = inputStream.read(buffer,0,BUFFER_SIZE))!=-1){
                                outputStream.write(buffer,0,read);
                                downloaded += read;
                                percentagesDownload = (int)((downloaded*100L)/fileSize);
                                System.out.println("Downloaded " + percentagesDownload + "% of the files..");
                            }
                            outputStream.close();
                            inputStream.close();
                            if (listener!=null){
                                listener.storageReport(path,link);
                            }
                            System.out.println("Downloaded!");
                        }else {
                            System.out.println("file is already exist");
                        }
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                    onStop();
                }
            }
        };
        runnable.run();
    }

    @Override
    public void scrapper() {
        onStart();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String url = GITHUB_API_BASE_URL + GITHUB_API_SEARCH_REPOSITORIES + keySeek;
                while (!isExit){
                    try {
                        URL direct = new URL(url);
                        HttpURLConnection httpURLConnection = (HttpURLConnection) direct.openConnection();
                        httpURLConnection.setRequestMethod("GET");
                        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
                        int respond = httpURLConnection.getResponseCode();
                        System.out.println("\n Sending 'GET' request for URL " + url);
                        System.out.println("Response code : " + respond);
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(httpURLConnection.getInputStream())
                        );
                        String inputLine;
                        StringBuilder response = new StringBuilder();
                        while ((inputLine = reader.readLine()) != null) {
                            response.append(inputLine + "\n");
                        }
                        reader.close();
                        System.out.println("Result of JSON object reading response ");
                        System.out.println("----------------------------------------");
                        JSONObject myResponse = new JSONObject(response.toString());
                        JSONArray array = myResponse.getJSONArray("items");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = (JSONObject) array.get(i);
                            String linkSource = object.getString("archive_url").replace("{archive_format}{/ref}",
                                    GITHUB_ZIP_DOWNLOAD);
                            System.out.println(linkSource);
                            if (i < 5) {
                                sources.add(linkSource);
                            }
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }finally {
                        saveLinks();
                        onStop();
                    }
                }
            }
        };
        runnable.run();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
