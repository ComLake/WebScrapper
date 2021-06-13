package com.example.comlakecrawler.repository;

import com.example.comlakecrawler.utils.LinkResources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourcesRepository extends JpaRepository<LinkResources,Long> {
}
