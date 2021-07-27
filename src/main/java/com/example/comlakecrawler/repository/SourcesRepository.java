package com.example.comlakecrawler.repository;

import com.example.comlakecrawler.utils.LinkResources;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SourcesRepository extends PagingAndSortingRepository<LinkResources,Long> {
    @Query("select s from LinkResources s where " +
            "concat(id, author, link, name, topic, websites)" +
            " like %?1%")
    Page<LinkResources> findByName(String name, Pageable pageable);
}
