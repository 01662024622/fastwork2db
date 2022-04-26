package com.fastwok.crawler.repository;

import com.fastwok.crawler.entities.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationRepository  extends JpaRepository<Authentication, Long> {
    Authentication findFirstByOrderByIdAsc();
}
