package com.example.comlakecrawler.service.downloader.target;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static com.example.comlakecrawler.service.config.Annotation.*;

public class KaggleCrawler extends Crawler {
    public KaggleCrawler() {
        sources = new ArrayList<>();
    }

    @Override
    public void download(String link, String path) {
        onStart();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (!isExit) {
                    try {
                        File file = new File(path);
                        if (!file.exists()) {
                            file.getParentFile().mkdirs();
                            file.createNewFile();
                            URL url = new URL(link);
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection.setRequestMethod("GET");
                            String auth = defaultKName + ":" + defaultKPass;
                            byte[] encodeAuth = Base64.encodeBase64(
                                    auth.getBytes(StandardCharsets.ISO_8859_1)
                            );
                            String authHeaderValue = "Basic " + new String(encodeAuth);
                            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
                            urlConnection.setRequestProperty("Authorization", authHeaderValue);
                            int responseCode = urlConnection.getResponseCode();
                            System.out.println("\nSending 'GET' Request to URL" + url);
                            System.out.println("Response code :" + responseCode);
                            double fileSize = (double) urlConnection.getContentLengthLong();
                            BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                            FileOutputStream fileOS = new FileOutputStream(file);
                            BufferedOutputStream outputStream = new BufferedOutputStream(fileOS, BUFFER_SIZE);
                            byte[] buffer = new byte[BUFFER_SIZE];
                            double downloaded = 0.00;
                            int percentageDownloaded;
                            int read;
                            while ((read = inputStream.read(buffer, 0, BUFFER_SIZE)) != -1) {
                                outputStream.write(buffer, 0, read);
                                downloaded += read;
                                percentageDownloaded = (int) ((downloaded * 100L) / fileSize);
                                System.out.println("Downloaded " + percentageDownloaded + "% of the file..");
                            }
                            outputStream.close();
                            inputStream.close();
                            if (listener != null) {
                                listener.storageReport(path, link);
                            }
                            System.out.println("Downloaded!");
                        } else {
                            System.out.println("file is already exist");
                        }
                    } catch (IOException e) {
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
                String k_direct = KAGGLE_API_BASE_URL + KAGGLE_API_SEARCH + keySeek + KAGGLE_PAGE;
                while (!isExit) {
                    try {
                        URL direct = new URL(k_direct);
                        HttpURLConnection urlConnection = null;
                        urlConnection = (HttpURLConnection) direct.openConnection();
                        urlConnection.setRequestMethod("GET");
                        String auth = defaultKName + ":" + defaultKPass;
                        byte[] encodeAuth = Base64.encodeBase64(
                                auth.getBytes(StandardCharsets.ISO_8859_1)
                        );
                        String authHeaderValue = "Basic " + new String(encodeAuth);
                        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
                        urlConnection.setRequestProperty("Authorization", authHeaderValue);
                        int responseCode = urlConnection.getResponseCode();
                        System.out.println("\nSending 'GET' Request to URL" + k_direct);
                        System.out.println("Response code :" + responseCode);
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(urlConnection.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine + "\n");
                        }
                        in.close();
                        System.out.println("Result of JSON Object reading response");
                        System.out.println("----------------------------------------");
                        JSONArray kaggleArray = new JSONArray(response.toString());
                        for (int i = 0; i < kaggleArray.length(); i++) {
                            JSONObject kaggleObject = (JSONObject) kaggleArray.get(i);
                            String dataDownload = kaggleObject.getString("ref");
                            StringBuffer kaggleDownload = new StringBuffer();
                            kaggleDownload.append(KAGGLE_API_BASE_URL);
                            kaggleDownload.append(KAGGLE_ZIP_DOWNLOAD);
                            kaggleDownload.append(dataDownload);
                            System.out.println(kaggleDownload.toString());
                            sources.add(kaggleDownload.toString());

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
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
