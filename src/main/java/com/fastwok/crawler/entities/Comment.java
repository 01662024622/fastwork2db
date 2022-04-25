package com.fastwok.crawler.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="fw_comments")
public class Comment {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Column(name="create_by")
    private String createBy;
    @Column(name="create_at")
    private Date createAt;
    @Column(name="content")
    private String content;
    @Column(name="task_id")
    private String taskId;
}
