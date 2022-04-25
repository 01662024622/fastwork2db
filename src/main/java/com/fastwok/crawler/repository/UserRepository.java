package com.fastwok.crawler.repository;

import com.fastwok.crawler.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT count(u) FROM User u WHERE u.email = ?1")
    Integer findUserByEmail(String email);
}
