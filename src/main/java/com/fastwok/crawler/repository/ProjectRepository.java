package com.fastwok.crawler.repository;

import com.fastwok.crawler.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p where p.status=0")
    List<Project> getProject();
}