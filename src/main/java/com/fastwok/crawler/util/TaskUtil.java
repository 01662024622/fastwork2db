package com.fastwok.crawler.util;

import com.fastwok.crawler.entities.Task;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
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
//        if (jsonObject.has("team")) {
//            if (jsonObject.getJSONObject("team").has("name")) {
//                task.setTeam(jsonObject.getJSONObject("team").getString("name"));
//            }
//        }
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

        task.setAssign(UserUtil.getListUser(jsonObject));
        return task;
    }

    public static HttpResponse<JsonNode> saveNotion(Task task) throws UnirestException {
        Date date = new Date();
        long timeMilli = date.getTime();
        return Unirest.post("https://api.notion.com/v1/pages")
                .header("Accept", "*/*")
                .header("Authorization", "Bearer secret_FSTMa6dNiG3fGlSpqD82j22yqwAhVWFRk0UqfyaUJCX")
                .header("x-fw", String.valueOf(timeMilli))
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.125 Safari/537.36")
                .header("Accept-Language", "en-US,en;q=0.9,vi;q=0.8")
                .header("Connection", "keep-alive")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("Notion-Version", "2021-08-16")
                .body("{" +
                        "    \"parent\": { \"database_id\": \"06cf32f5a8604100854d82684c48cffd\" }," + getProperty(task) +
                        "}")
                .asJson();

    }

    public static HttpResponse<JsonNode> updateNotion(Task task) throws UnirestException {
        Date date = new Date();
        long timeMilli = date.getTime();
        log.info(task.getStatus());
        return Unirest.post("https://api.notion.com/v1/pages/" + task.getNotionId())
                .header("Accept", "*/*")
                .header("Authorization", "Bearer secret_FSTMa6dNiG3fGlSpqD82j22yqwAhVWFRk0UqfyaUJCX")
                .header("x-fw", String.valueOf(timeMilli))
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.125 Safari/537.36")
                .header("Accept-Language", "en-US,en;q=0.9,vi;q=0.8")
                .header("Connection", "keep-alive")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("Notion-Version", "2021-08-16")
                .body("{" + getProperty(task) + "}")
                .asJson();

    }

    private static String getProperty(Task task) {

        String startDate;
        String endDate;
        String over = "";
        if (task.getStartDate() == null) startDate = "";
        else startDate = getDateProperty("Ngày bắt đầu",new Date(task.getStartDate()));

        if (task.getEnd_date() == null) endDate = "";
        else {
            endDate = getDateProperty("Ngày kết thúc",new Date(task.getEnd_date()));
            if (task.getCompleteDate() != null) {
                if (task.getCompleteDate() > task.getEnd_date()+3600000) {
                    Long overDay = (task.getCompleteDate() - task.getEnd_date()) / 1000 / 3600;
                    over = "\uD83D\uDD34 "+"Quá hạn: " + overDay + " giờ";
                }
            } else {
                Date date = new Date();
                long timeMilli = date.getTime();
                if (timeMilli > task.getEnd_date()) {
                    if (task.getOverTask() == null)
                        over = "\uD83D\uDD34 "+"Quá hạn: " + Math.ceil(((timeMilli - task.getEnd_date())/3600000)) + " giờ";
                    else over = "\uD83D\uDD34 "+ task.getOverTask();
                }
            }
        }

        String res = "\"properties\": {" +
                "\"Người tạo\": {" +
                "\"rich_text\": [" +
                "    {" +
                "        \"text\": {" +
                "            \"content\": \"" + task.getCreateBy() + "\"" +
                "        }" +
                "    }" +
                "]" +
                "                }," +
                "                \"Tiến độ\": {" +
                "\"rich_text\": [" +
                "    {" +
                "        \"text\": {" +
                "            \"content\": \"" + task.getCompleted() + "%\"" +
                "        }" +
                "    }" +
                "]" +
                "                }," +
                "                \"Mô tả\": {" +
                "\"rich_text\": [" +
                "    {" +
                "        \"text\": {" +
                "            \"content\": \"" + task.getDescription() + "\"" +
                "        }" +
                "    }" +
                "]" +
                "                }," +
                "                \"Dự án\": {" +
                "\"rich_text\": [" +
                "    {" +
                "        \"text\": {" +
                "            \"content\": \"" + task.getProject() + "\"" +
                "        }" +
                "    }" +
                "]" +
                "                }," +
                "                \"Link\": {" +
                "\"url\": \"https://app.fastwork.vn/tasks#!/assigned/?view=danhsach&idcv=" + task.getId() + "\"" +
                "                }," +
                "                \"Trạng thái\": {" +
                "\"select\": {" +
                "    \"name\": \"" + task.getStatus() + "\"" +
                "}" +
                "                }," +
                "                \"Quá hạn\": {" +
                "\"rich_text\": [" +
                "    {" +
                "        \"text\": {" +
                "            \"content\": \"" + over + "\"" +
                "        }" +
                "    }" +
                "]" +
                "                }," +

                "                \"Người thực hiện\": {" +
                "\"rich_text\": [" +
                "    {" +
                "        \"text\": {" +
                "            \"content\": \"" + task.getAssign() + "\"" +
                "        }" +
                "    }" +
                "]" +
                "                }," +
                startDate +
                endDate +
                "                \"Công việc\": {" +
                "\"title\": [" +
                "    {" +
                "        \"text\": {" +
                "            \"content\": \"" + task.getTitle() + "\"" +
                "        }" +
                "    }" +
                "]" +
                "                }" +
                "}";

        log.info(res);
        return res;
    }
    private static String getDateProperty(String property,Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = formatter.format(date);
        return "                \""+property+"\": {" +
                "\"date\": {" +
                "\"start\": \""+strDate+"+07:00\"" +
                "}" +
                "                },";
    }

}
