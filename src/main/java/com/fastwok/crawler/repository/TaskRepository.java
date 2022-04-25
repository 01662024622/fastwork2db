package com.fastwok.crawler.repository;

import com.fastwok.crawler.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TaskRepository extends JpaRepository<Task, String> {
    Task getById(String id);
}
