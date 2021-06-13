package com.example.comlakecrawler.service.downloader;

import com.example.comlakecrawler.utils.LinkResources;

import java.util.List;

public interface SourcesService {
    List<LinkResources>getAllResources();
    void addSomeSourcesLink(LinkResources linkResources);
}
