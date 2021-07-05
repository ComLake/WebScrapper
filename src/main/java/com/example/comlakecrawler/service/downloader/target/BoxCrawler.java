package com.example.comlakecrawler.service.downloader.target;

import com.box.sdk.*;
import com.example.comlakecrawler.service.downloader.CrawlerInterface;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BoxCrawler {
    private CrawlerInterface listener;
    private static final int BUFFER_SIZE = 4096;
    private static final String USER_ID = "16373928354";
    private static final int MAX_DEPTH = 1;
    private static final int MAX_CACHE_ENTRIES = 100;
    private boolean isExit;
    private String urlSharedFile;
    private static BoxDeveloperEditionAPIConnection api;
    private String topic;
    private ArrayList<String> sources;
    private static final String path = "D:\\save\\sources\\";

    public BoxCrawler() {
        // Turn off logging to prevent polluting the output.
        Logger.getLogger("com.box.sdk").setLevel(Level.SEVERE);
        isExit = false;
        IAccessTokenCache accessTokenCache = new InMemoryLRUAccessTokenCache(MAX_CACHE_ENTRIES);
        try {
            Reader reader = new FileReader("src/main/resources/static/lib/config.json");
            BoxConfig config = BoxConfig.readFrom(reader);
            api = BoxDeveloperEditionAPIConnection.getAppUserConnection(USER_ID,
                    config,
                    accessTokenCache);
            BoxUser.Info userInfo = BoxUser.getCurrentUser(api).getInfo();
            System.out.format("Welcome, %s!\n\n", userInfo.getName());
            sources = new ArrayList<>();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUrlSharedFile() {
        return urlSharedFile;
    }

    public void setUrlSharedFile(String urlSharedFile) {
        this.urlSharedFile = urlSharedFile;
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

    public void searchFileInURLSharing() {
        BoxItem.Info info = BoxFolder.getSharedItem(api, urlSharedFile);
        System.out.println(info.getID() + " : " + info.getName() + ", Owner: " + info.getOwnedBy().getID() + ", " + info.getResource());
        BoxFolder boxFolder = (BoxFolder) info.getResource();
        Iterable<BoxItem.Info> folderItems = boxFolder.getChildren();
        for (BoxItem.Info item : folderItems) {
            if (item instanceof BoxFile.Info && item.getName().contains(topic)) {
                String jsonResult = "[{\"url\":\"" + urlSharedFile + "\",\"name\":\"" + item.getName() + "\"}]";
                System.out.println(jsonResult);
                sources.add(jsonResult);
            }
        }
        saveLink();
    }
    private String sharedLinkConfig(String fileName){
        String url = null;
        BoxItem.Info info = BoxFolder.getSharedItem(api, urlSharedFile);
        BoxFolder boxFolder = (BoxFolder) info.getResource();
        Iterable<BoxItem.Info> folderItems = boxFolder.getChildren();
        for (BoxItem.Info item : folderItems) {
            if (item instanceof BoxFile.Info && item.getName().equals(fileName)) {
                BoxFile boxFile = ((BoxFile.Info) item).getResource();
                url = String.valueOf(boxFile.getDownloadURL());
            }
        }
        return url;
    }
    public void downloadWithSharedLink(String fileName) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (!isExit) {
                    File file = new File(path + "box_"+fileName);
                    if (!file.exists()) {
                        file.getParentFile().mkdirs();
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    URL directSharedLink = null;
                    try {
                        if (sharedLinkConfig(fileName)!=null){
                            directSharedLink = new URL(sharedLinkConfig(fileName));
                            HttpURLConnection urlConnection = (HttpURLConnection) directSharedLink.openConnection();
                            urlConnection.setRequestMethod("GET");
                            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
                            int responseCode = urlConnection.getResponseCode();
                            System.out.println("\nSending 'GET' Request to URL" + urlSharedFile);
                            System.out.println("Response code : " + responseCode);
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
                                System.out.println("Downloaded" + percentageDownloaded + "% of the file..");
                            }
                            outputStream.close();
                            inputStream.close();
                        }else {
                            System.out.println("Unsolved protocol url connection");
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        isExit = true;
                    }
                }
            }
        };
        runnable.run();
    }
    private void crawlerSearchResult(BoxSearchParameters boxSearchParam, BoxSearch boxSearch) {
        //Set up result Partial Object
        PartialCollection<BoxItem.Info> searchResult;
        //Starting point of the result set
        long offset = 0;
        //Number of results that would be pulled back
        long limit = 1000;
        //Storing the full size of the results
        long fullSizeOfResult = 0;

        while (offset <= fullSizeOfResult) {
            searchResult = boxSearch.searchRange(offset, limit, boxSearchParam);
            fullSizeOfResult = searchResult.fullSize();
            System.out.println("offset: " + offset + " of fullSizeOfResult: " + fullSizeOfResult);
            printSearchResults(searchResult);
            offset += limit;
        }
    }

    private void printSearchResults(PartialCollection<BoxItem.Info> searchResult) {
        //Crawl the folder
        System.out.println("--=Results fullResultSize: " + searchResult.fullSize() + "==--");
        for (BoxItem.Info info : searchResult) {
//            System.out.println("File Found: "+info.getID()+" : "+info.getName()+", Owner: "+info.getOwnedBy().getID());
            sources.add("boxnet:box:" + info.getID() + ":" + info.getName() + ":" + info.getOwnedBy().getID() + ":" + info.getOwnedBy().getName());
        }
//        saveLink();
    }

    public void downloadFile(String id) {
        BoxFile file = new BoxFile(api, id);
        BoxFile.Info info = file.getInfo();

        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(new File(path + info.getName() + info.getType().replace("file", "")));
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

    private void searchByDescription(BoxSearchParameters boxSearchParam, BoxSearch boxSearch) {
        System.out.println("************Search by Description************");
        List<String> contentTypes = new ArrayList<>();
        contentTypes.add("");
        boxSearchParam.clearParameters();
        boxSearchParam.setContentTypes(contentTypes);
        boxSearchParam.setQuery(topic);
        crawlerSearchResult(boxSearchParam, boxSearch);
    }

    private void ownerIdFilterExample(BoxSearchParameters boxSearchParam, BoxSearch boxSearch) {
        System.out.println("***********Owner Id's Filter Search***********");
        List<String> ownerUserIds = new ArrayList<>();
        ownerUserIds.add(USER_ID);
        boxSearchParam.clearParameters();
        boxSearchParam.setQuery(topic);
        boxSearchParam.setOwnerUserIds(ownerUserIds);
        crawlerSearchResult(boxSearchParam, boxSearch);
    }

    private void saveLink() {
        if (listener != null) {
            listener.updateSources(topic, sources);
        }
    }
}
