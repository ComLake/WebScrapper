package com.example.comlakecrawler.service.downloader;

import com.example.comlakecrawler.repository.SourcesRepository;
import com.example.comlakecrawler.utils.LinkResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SourcesServiceImpl implements SourcesService{
    @Autowired
    private SourcesRepository sourcesRepository;
    @Override
    public List<LinkResources> getAllResources() {
        return sourcesRepository.findAll();
    }

    @Override
    public void addSomeSourcesLink(LinkResources linkResources) {
        this.sourcesRepository.save(linkResources);
    }
}
