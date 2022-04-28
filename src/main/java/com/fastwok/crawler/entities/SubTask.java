package com.fastwok.crawler.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name="fw_sub_task")
public class SubTask {
    @Id
    private String id;
    @Column(length = 1000)
    private String title;
    @Column(name = "complete_date")
    private String completeDate;
    @Column(name = "to_date")
    private String toDate;
    @Column(name = "create_date")
    private String createDate;
    @Column(name = "from_date")
    private String fromDate;
    private String completeBy;
    @Column(name = "task_id")
    private String taskId;
}
