package com.example.comlakecrawler.repository;

import com.example.comlakecrawler.utils.LinkResources;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface SourcesRepository extends JpaRepository<LinkResources,Long> {
    @Query("select s from LinkResources s where topic like %?1%" +
            "or websites like %?1%" +
            "or name like %?1%")
    List<LinkResources> findByName(String name);
}
