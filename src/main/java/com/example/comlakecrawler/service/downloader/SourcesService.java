package com.example.comlakecrawler.service.downloader;

import com.example.comlakecrawler.utils.LinkResources;
import com.example.comlakecrawler.utils.SourcesRegistration;

import java.util.List;

public interface SourcesService {
    List<LinkResources>getAllResources();
    void addSomeSourcesLink(SourcesRegistration sourcesRegister);
}
