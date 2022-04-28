package com.fastwok.crawler.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "fw_tasks")
public class Task {
    @Id
    private String id;
    @Column(length = 1000)
    private String title;
    @Column(length = 5000)
    private String description;
    @Column(name = "create_by")
    private String createBy;
    private String project;
    private String scenario;
    @Column(name = "start_date")
    private String startDate;
    @Column(name = "modified_date")
    private Long modifiedDate;
    @Column(name = "end_date")
    private String endDate;
    @Column(name = "start_date_long")
    private long endDateLong;
    private String status;
    private String proccess;
    private Integer overdue;
    @Column(name = "complete_date")
    private String completeDate;

    @Column(name = "complete_long")
    private Long completeLong;
    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "over_task", length = 5000)
    private String overTask;
    @Column(name = "completed")
    private Integer completed;
    private String link;
    private String project_id;
}
