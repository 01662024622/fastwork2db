package com.fastwok.crawler.repository;

import com.fastwok.crawler.entities.TaskUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TaskUserRepository extends JpaRepository<TaskUser, Long> {
    @Query("SELECT t FROM TaskUser t WHERE  t.taskId= ?1 AND t.user=?2")
    TaskUser checkExist(String id, String user);
}
