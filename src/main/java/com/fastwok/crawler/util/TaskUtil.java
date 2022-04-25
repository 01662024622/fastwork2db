package com.fastwok.crawler.util;

import com.fastwok.crawler.entities.SubTask;
import com.fastwok.crawler.entities.Task;
import com.fastwok.crawler.entities.TaskUser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
public class TaskUtil {
    public static Task convertToTask(JSONObject jsonObject, Task task) {
        if (jsonObject.has("_id")) {
            task.setId(jsonObject.getString("_id"));
        }
        if (jsonObject.has("title")) {
            task.setTitle(jsonObject.getString("title"));
        }
        if (jsonObject.has("description")) {
            task.setDescription(jsonObject.getString("description").replace("\n", ""));
        }
        if (jsonObject.has("cretedBy")) {
            if (jsonObject.getJSONObject("cretedBy").has("name")) {
                task.setCreateBy(jsonObject.getJSONObject("cretedBy").getString("name"));
            }
        }
        if (jsonObject.has("project")) {
            if (jsonObject.getJSONObject("project").has("name")) {
                task.setProject(jsonObject.getJSONObject("project").getString("name"));
            }
        }
        if (jsonObject.has("workitem")) {
            if (jsonObject.getJSONObject("workitem").has("name")) {
                task.setScenario(jsonObject.getJSONObject("workitem").getString("name"));
            }
        }
        if (jsonObject.has("start_date")) {
            if (jsonObject.get("start_date").toString().length() > 6)
                task.setStartDate(jsonObject.getLong("start_date"));
        }
        if (jsonObject.has("end_date")) {
            if (jsonObject.get("end_date").toString().length() > 6)
                task.setEnd_date(jsonObject.getLong("end_date"));
        }
        if (jsonObject.has("modifiedDate")) {
            if (!jsonObject.isNull("modifiedDate"))
                task.setModifiedDate(jsonObject.getLong("modifiedDate"));
        }
        if (jsonObject.has("status")) {
            if (jsonObject.get("status").toString().equals(""))
                task.setStatus("Chưa thực hiện");
            else task.setStatus(jsonObject.get("status").toString());
        }
        if (jsonObject.has("proccess")) {
            task.setProccess(jsonObject.get("proccess").toString());
        }
        if (jsonObject.has("overdue")) {
            if (jsonObject.getBoolean("overdue")) {
                task.setOverdue(0);
            } else task.setOverdue(1);
        }
        if (jsonObject.has("ngay_hoan_thanh")) {
            if (jsonObject.get("ngay_hoan_thanh").toString().length() > 6) {
                task.setCompleteDate(jsonObject.getLong("ngay_hoan_thanh"));
            }

        }
        if (jsonObject.has("parentJob")) {
            task.setParentId(jsonObject.getString("parentJob"));
        }
        if (jsonObject.has("completed")) {
            int completed;
            if (jsonObject.isNull("completed")) completed = 0;
            else completed = jsonObject.getInt("completed");
            task.setCompleted(completed);
        }
        task.setOverTask(SubTaskUtil.getOverSubTask(jsonObject));

        return task;
    }

    public static List<TaskUser> getListUser(JSONObject jsonValues) {
        String id = jsonValues.getString("_id");
        if (jsonValues.has("assignTo")) {
            if (jsonValues.getJSONArray("assignTo").length() > 0) {
                List<TaskUser> users = new ArrayList<>();
                for (int z = 0; z < jsonValues.getJSONArray("assignTo").length(); z++) {
                    JSONObject jsonObject = jsonValues.getJSONArray("assignTo").getJSONObject(z);
                    TaskUser user = new TaskUser();
                    user.setTaskId(id);
                    user.setUser(jsonObject.getString("name"));
                    users.add(user);
                }
                return users;
            }
        }
        return null;
    }

    public static List<SubTask> getListSubTask(JSONObject jsonValues) {
        List<SubTask> subTasks = new ArrayList<>();
        String id = jsonValues.getString("_id");
        if (jsonValues.has("checklists")) {
            if (jsonValues.getJSONArray("checklists").length() == 0) return null;
            if (jsonValues.getJSONArray("checklists").length() > 1) {
                if (jsonValues.getJSONArray("checklists").getJSONObject(1).has("checkItems")) {
                    if (jsonValues.getJSONArray("checklists").getJSONObject(1).getJSONArray("checkItems").length() > 0) {
                        JSONArray jsonArr = jsonValues.getJSONArray("checklists").getJSONObject(1).getJSONArray("checkItems");

                        for (int x = 0; x < jsonArr.length(); x++) {
                            SubTask subTask = new SubTask();
                            subTask.setTaskId(id);
                            subTask.setTitle(jsonArr.getJSONObject(x).getString("title"));
                            subTask.setId(jsonArr.getJSONObject(x).getString("id"));
                            if (jsonArr.getJSONObject(x).has("completedBy"))
                                subTask.setCompleteBy(jsonArr.getJSONObject(x).getJSONObject("completedBy").getString("name"));
                            if (jsonArr.getJSONObject(x).has("completedDate")) {
                                if (jsonArr.getJSONObject(x).get("completedDate").toString().length() > 6) {
                                    subTask.setCompleteDate(jsonArr.getJSONObject(x).getLong("completedDate"));
                                }

                            }
                            if (jsonArr.getJSONObject(x).has("from_date")) {
                                if (jsonArr.getJSONObject(x).get("from_date").toString().length() > 6) {
                                    subTask.setFromDate(jsonArr.getJSONObject(x).getLong("from_date"));
                                }

                            }
                            if (jsonArr.getJSONObject(x).has("to_date")) {
                                if (jsonArr.getJSONObject(x).get("to_date").toString().length() > 6) {
                                    subTask.setToDate(jsonArr.getJSONObject(x).getLong("to_date"));
                                }

                            }
                            if (jsonArr.getJSONObject(x).has("createdDate")) {
                                if (jsonArr.getJSONObject(x).get("createdDate").toString().length() > 6) {
                                    subTask.setCreateDate(jsonArr.getJSONObject(x).getLong("createdDate"));
                                }

                            }
                            subTasks.add(subTask);
                        }
                    }
                }
            } else {
                if (jsonValues.getJSONArray("checklists").getJSONObject(0).has("checkItems")) {
                    if (jsonValues.getJSONArray("checklists").getJSONObject(0).getJSONArray("checkItems").length() > 0) {
                        JSONArray jsonArr = jsonValues.getJSONArray("checklists").getJSONObject(0).getJSONArray("checkItems");
                        for (int x = 0; x < jsonArr.length(); x++) {
                            SubTask subTask = new SubTask();
                            subTask.setTaskId(id);
                            subTask.setTitle(jsonArr.getJSONObject(x).getString("title"));
                            subTask.setId(jsonArr.getJSONObject(x).getString("id"));
                            if (jsonArr.getJSONObject(x).has("completedBy"))
                                subTask.setCompleteBy(jsonArr.getJSONObject(x).getJSONObject("completedBy").getString("name"));
                            if (jsonArr.getJSONObject(x).has("completedDate")) {
                                if (jsonArr.getJSONObject(x).get("completedDate").toString().length() > 6) {
                                    subTask.setCompleteDate(jsonArr.getJSONObject(x).getLong("completedDate"));
                                }

                            }
                            if (jsonArr.getJSONObject(x).has("from_date")) {
                                if (jsonArr.getJSONObject(x).get("from_date").toString().length() > 6) {
                                    subTask.setFromDate(jsonArr.getJSONObject(x).getLong("from_date"));
                                }

                            }
                            if (jsonArr.getJSONObject(x).has("to_date")) {
                                if (jsonArr.getJSONObject(x).get("to_date").toString().length() > 6) {
                                    subTask.setToDate(jsonArr.getJSONObject(x).getLong("to_date"));
                                }

                            }
                            if (jsonArr.getJSONObject(x).has("createdDate")) {
                                if (jsonArr.getJSONObject(x).get("createdDate").toString().length() > 6) {
                                    subTask.setCreateDate(jsonArr.getJSONObject(x).getLong("createdDate"));
                                }

                            }
                            subTasks.add(subTask);
                        }
                    }
                }
            }
            return subTasks;
        }
        return null;
    }


}
