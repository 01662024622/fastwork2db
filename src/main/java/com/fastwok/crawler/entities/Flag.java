package com.fastwok.crawler.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "fw_flags")
public class Flag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "type")
    private String type = "fast work to notion";
    @Column(name = "type_flag")
    private String typeFlag="date type long";
    @Column(name = "flag")
    private Long flag;
}
