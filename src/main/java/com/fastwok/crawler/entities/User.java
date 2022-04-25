package com.fastwok.crawler.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="fw_user_fastwork_project")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String email;
    private String name;
}
