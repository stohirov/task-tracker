package entity;

import service.TaskService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {

    private int id;

    private String description;

    private Status status;

    private String createdAt;

    private String updatedAt;

    public Task(String description) {
        this.id = TaskService.randomId();
        this.description = description;
        this.status = Status.TODO;
        this.createdAt = getCurrentTimestamp();
        this.updatedAt = getCurrentTimestamp();
    }

    public Task(int id, String description, String currentStatus, String createdAt, String updatedAt) {
        this.id = id;
        this.description = description;
        this.status = Status.valueOf(currentStatus);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private String getCurrentTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return LocalDateTime.now().format(formatter);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id: " + id +
                ", description: '" + description + '\'' +
                ", status: " + status +
                ", createdAt: '" + createdAt + '\'' +
                ", updatedAt: '" + updatedAt + '\'' +
                '}';
    }

    public String toJson() {
        return "{" +
                "\"id\": \"" + this.id + "\"," +
                "\"description\": \"" + this.description + "\"," +
                "\"status\": \"" + this.status + "\"," +
                "\"createdAt\": \"" + this.createdAt + "\"," +
                "\"updatedAt\": \"" + this.updatedAt + "\"" +
                "}";
    }

    public static Task fromJson(String json) {
        String[] parts = json.replace("{", "")
                .replace("}", "")
                .split(",");
        int id = Integer.parseInt(parts[0].split(":")[1].replace("\"", "").trim());
        String description = parts[1].split(":")[1].replace("\"", "").trim();
        String currentStatus = parts[2].split(":")[1].replace("\"", "").trim();
        String createdAt = parts[3].split(":")[1].replace("\"", "").trim();
        String updatedAt = parts[4].split(":")[1].replace("\"", "").trim();
        return new Task(id, description, currentStatus, createdAt, updatedAt);
    }
}
