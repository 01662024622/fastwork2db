package com.fastwok.crawler.util;

import com.fastwok.crawler.entities.SubTask;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SubTaskUtil {
//    public static List<SubTask> convertSubTask(JSONObject jsonValues, String taskId) {
//        List<SubTask> subTasks = new ArrayList<>();
//        if (jsonValues.has("checklists")) {
//            if (jsonValues.getJSONArray("checklists").length() > 1) {
//                if (jsonValues.getJSONArray("checklists").getJSONObject(1).has("checkItems")) {
//                    if (jsonValues.getJSONArray("checklists").getJSONObject(1).getJSONArray("checkItems").length() > 0) {
//                        JSONArray jsonArr = jsonValues.getJSONArray("checklists").getJSONObject(1).getJSONArray("checkItems");
//                        for (int x = 0; x < jsonArr.length(); x++) {
//                            SubTask subTask = new SubTask();
//                            subTask.setTaskId(taskId);
//                            subTask.setId(jsonArr.getJSONObject(x).getString("id"));
//                            subTask.setTitle(jsonArr.getJSONObject(x).getString("title"));
//                            if (jsonArr.getJSONObject(x).has("completedDate")) {
//                                if (jsonArr.getJSONObject(x).get("completedDate").toString().length() > 6) {
//                                    long complete = jsonArr.getJSONObject(x).getLong("completedDate");
//                                    subTask.setCompleteDate(new Date(complete));
//                                }
//                            }
//                            if (jsonArr.getJSONObject(x).has("createdDate")) {
//                                if (jsonArr.getJSONObject(x).get("createdDate").toString().length() > 6)
//                                    subTask.setCreateDate(new Date(jsonArr.getJSONObject(x).getLong("createdDate")));
//                            }
//                            if (jsonArr.getJSONObject(x).has("from_date")) {
//                                if (jsonArr.getJSONObject(x).get("from_date").toString().length() > 6)
//                                    subTask.setFromDate(new Date(jsonArr.getJSONObject(x).getLong("from_date")));
//                            }
//                            if (jsonArr.getJSONObject(x).has("to_date")) {
//                                if (jsonArr.getJSONObject(x).get("to_date").toString().length() > 6)
//                                    subTask.setToDate(new Date(jsonArr.getJSONObject(x).getLong("to_date")));
//                            }
//                            if (jsonArr.getJSONObject(x).has("completedBy")) {
//                                if (jsonArr.getJSONObject(x).getJSONObject("completedBy").has("name")) {
//                                    subTask.setCompleteBy(jsonArr.getJSONObject(x).getJSONObject("completedBy").get("name").toString());
//                                }
//                            }
//                            subTasks.add(subTask);
//                        }
//                    }
//                }
//            }
//        }
//        return subTasks;
//    }

    public static String getOverSubTask(JSONObject jsonValues) {
        if (jsonValues.has("checklists")) {
            if (jsonValues.getJSONArray("checklists").length() ==0) return null;
            if (jsonValues.getJSONArray("checklists").length() > 1) {
                if (jsonValues.getJSONArray("checklists").getJSONObject(1).has("checkItems")) {
                    if (jsonValues.getJSONArray("checklists").getJSONObject(1).getJSONArray("checkItems").length() > 0) {
                        JSONArray jsonArr = jsonValues.getJSONArray("checklists").getJSONObject(1).getJSONArray("checkItems");
                        for (int x = 0; x < jsonArr.length(); x++) {
                            if (jsonArr.getJSONObject(x).getInt("completed") == 0) {
                                return "Bước " + (x + 1) + ": " + jsonArr.getJSONObject(x).getString("title");
                            }
                        }
                    }
                }
            } else {
                if (jsonValues.getJSONArray("checklists").getJSONObject(0).has("checkItems")) {
                    if (jsonValues.getJSONArray("checklists").getJSONObject(0).getJSONArray("checkItems").length() > 0) {
                        JSONArray jsonArr = jsonValues.getJSONArray("checklists").getJSONObject(0).getJSONArray("checkItems");
                        for (int x = 0; x < jsonArr.length(); x++) {
                            if (jsonArr.getJSONObject(x).getInt("completed") == 0) {
                                return "Bước " + (x + 1) + ": " + jsonArr.getJSONObject(x).getString("title");
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
