package com.fastwok.crawler.entities;

import lombok.Data;

import java.util.List;

@Data
public class Tuple {
    private Task task;
    private List<TaskUser> taskUsers;
}
