package com.fastwok.crawler.repository;

import com.fastwok.crawler.entities.Flag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FlagRepository extends JpaRepository<Flag, Long> {
    @Query("SELECT max(f.flag) FROM Flag f")
    Long getMaxOfFlag();
}