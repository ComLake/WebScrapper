package com.example.comlakecrawler.service.downloader;

import com.example.comlakecrawler.utils.LinkResources;
import com.example.comlakecrawler.utils.SourcesRegistration;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.List;

public interface SourcesService {
    List<LinkResources>getAllResources();
    void addSomeSourcesLink(SourcesRegistration sourcesRegister);
    LinkResources getLinksById(long id);
    void downloadSources(long id);
    List<LinkResources> findByName(String name);
}
