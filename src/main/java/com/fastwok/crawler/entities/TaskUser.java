package com.fastwok.crawler.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="fw_task_user")
public class TaskUser {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Column(name="user_email")
    private String userEmail;
    @Column(name="task_id")
    private String taskId;

}
