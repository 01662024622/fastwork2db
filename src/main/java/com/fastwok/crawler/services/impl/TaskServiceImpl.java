package com.fastwok.crawler.services.impl;

import com.fastwok.crawler.entities.Project;
import com.fastwok.crawler.entities.SubTask;
import com.fastwok.crawler.entities.Task;
import com.fastwok.crawler.entities.TaskUser;
import com.fastwok.crawler.repository.*;
import com.fastwok.crawler.services.isservice.TaskService;
import com.fastwok.crawler.util.TaskUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    SubTaskRepository subTaskRepository;
    @Autowired
    TaskUserRepository taskUserRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    AuthenticationRepository authenticationRepository;
    String AUTHEN = "";

    @Override
    public void getData(String string) {
        AUTHEN=authenticationRepository.findFirstByOrderByIdAsc().getAuthentication();
        List<Project> projects = projectRepository.getProject();
        projects.forEach(project -> {
            try {
                running(string, project.getProject_id());
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        });
    }

    public void running(String string, String projectId) throws UnirestException {
        HttpResponse<JsonNode> response = getListTask(string, projectId);
        JSONObject res = new JSONObject(response.getBody());
        log.info(res.toString());
        JSONArray jsonArray = res.getJSONObject("object").getJSONArray("result");
        int total = jsonArray.length();
        for (int n = 0; n < total; n++) {
            JSONArray tasks = jsonArray.getJSONObject(n).getJSONArray("tasks");
            for (int y = 0; y < tasks.length(); y++) {
                List<TaskUser> taskUsers = new ArrayList<>();
                List<SubTask> subTasks = new ArrayList<>();
                String id = tasks.getJSONObject(y).get("_id").toString();
                Task getTask = taskRepository.getById(id);
                JSONObject jsonValues = new JSONObject(getDataDetail(id).getBody()).getJSONObject("result");
                if (getTask == null) {
                    getTask = new Task();
                } else if (jsonValues.getLong("modifiedDate") < getTask.getModifiedDate()) return;

                getTask = TaskUtil.convertToTask(jsonValues, getTask);
                taskUsers = TaskUtil.getListUser(jsonValues);
                subTasks = TaskUtil.getListSubTask(jsonValues);
                taskRepository.save(getTask);
                if (taskUsers != null) {
                    taskUsers.forEach(element -> {
                        TaskUser taskUser = taskUserRepository.checkExist(element.getTaskId(), element.getUser());
                        if (taskUser == null) {
                            taskUserRepository.save(element);
                        }
                    });
                }
                if (subTasks != null) {
                    subTasks.forEach(element -> {
                        subTaskRepository.save(element);
                    });
                }
            }

        }
    }

    private HttpResponse<JsonNode> getListTask(String string, String projectId)
            throws UnirestException {
        Date date = new Date();
        long timeMilli = date.getTime();
        log.info(String.valueOf(timeMilli));
        return Unirest.post("https://work.fastwork.vn:6014/Project/Tasks/" + projectId + "?orgid=5efef3dd5a51cf1c10fab0e4&fromDate=1500000000000&toDate=1693885199999&orderbyField=cretedDate&orderbyType=desc" + string)
                .header("Accept", "*/*")
                .header("Authorization", AUTHEN)
                .header("x-fw", String.valueOf(timeMilli))
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.125 Safari/537.36")
                .header("Accept-Language", "en-US,en;q=0.9,vi;q=0.8")
                .header("Referer", "https://app.fastwork.vn/")
                .header("Origin", "https://app.fastwork.vn")
                .header("Host", "crm.fastwork.vn:6014")
                .header("Connection", "keep-alive")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Content-Type", "application/json")
                .header("Sec-Fetch-Dest", "empty")
                .header("Sec-Fetch-Mode", "cors")
                .header("Sec-Fetch-Site", "same-site")
                .body("{}")
                .asJson();
    }

    private HttpResponse<String> getDataDetail(String taskId) throws UnirestException {
        Date date = new Date();
        long timeMilli = date.getTime();
        return Unirest.get("https://work.fastwork.vn:6014/Job/" + taskId + "/detail/5efef3dd5a51cf1c10fab0e4")
                .queryString("type", "preview")
                .header("Accept", "*/*")
                .header("Authorization", AUTHEN)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.125 Safari/537.36")
                .header("Accept-Language", "en-US,en;q=0.9,vi;q=0.8")
                .header("Referer", "https://app.fastwork.vn/")
                .header("Origin", "https://app.fastwork.vn")
                .header("Host", "work.fastwork.vn:6014")
                .header("Connection", "keep-alive")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("x-fw", String.valueOf(timeMilli))
                .header("Sec-Fetch-Dest", "empty")
                .header("Sec-Fetch-Mode", "cors")
                .header("Sec-Fetch-Site", "same-site")
                .asString();
    }

    private HttpResponse<String> getDataDetailComment(String taskId) throws UnirestException {
        Date date = new Date();
        long timeMilli = date.getTime();
        return Unirest.get("https://service.fastwork.vn:6006/CRMComments/5efef3dd5a51cf1c10fab0e4?next=999999999999&skip=0&job_id=" + taskId)
                .queryString("type", "preview")
                .header("Accept", "*/*")
                .header("Authorization", AUTHEN)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.125 Safari/537.36")
                .header("Accept-Language", "en-US,en;q=0.9,vi;q=0.8")
                .header("Referer", "https://app.fastwork.vn/")
                .header("Origin", "https://app.fastwork.vn")
                .header("Host", "service.fastwork.vn:6006")
                .header("Connection", "keep-alive")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("x-fw", String.valueOf(timeMilli))
                .header("Sec-Fetch-Dest", "empty")
                .header("Sec-Fetch-Mode", "cors")
                .header("Sec-Fetch-Site", "same-site")
                .asString();
    }
}
