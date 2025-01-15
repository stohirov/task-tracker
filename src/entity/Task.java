package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {

    private static int idCounter = 0;

    private int id;

    private String description;

    private Status status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Task(String description) {
        this.id = generateId();
        this.description = description;
        this.status = Status.TODO;
        this.createdAt = getCurrentTimestamp();
        this.updatedAt = getCurrentTimestamp();
    }

    public Task(int id, String description, String currentStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.description = description;
        this.status = Status.valueOf(currentStatus);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private LocalDateTime getCurrentTimestamp() {
        return LocalDateTime.now();
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
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

    public static synchronized int generateId() {
        return ++idCounter;
    }
}
