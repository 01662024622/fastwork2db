package com.fastwok.crawler.repository;

import com.fastwok.crawler.entities.TaskUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskUserRepository extends JpaRepository<TaskUser, Long> {
}
