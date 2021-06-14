package com.example.comlakecrawler.service.downloader;

import java.util.ArrayList;

public interface CrawlerInterface {
    void storageReport(String file,String link);
    void updateSources(String topic,ArrayList<String>sourcesLink);
}
