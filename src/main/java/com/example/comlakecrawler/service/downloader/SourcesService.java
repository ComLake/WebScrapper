package com.example.comlakecrawler.service.downloader;

import com.example.comlakecrawler.utils.LinkResources;
import com.example.comlakecrawler.utils.SourcesRegistration;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.List;

public interface SourcesService {
    Page<LinkResources>getAllResources(int pageNumber);
    void addSomeSourcesLink(SourcesRegistration sourcesRegister);
    LinkResources getLinksById(long id);
    void downloadSources(long id, String token);
    Page<LinkResources> findByName(String name,int pageNumber);
}
