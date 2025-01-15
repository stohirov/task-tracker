package service;

import entity.Status;
import entity.Task;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TaskService {

    public static String FILE_PATH = System.getProperty("user.home") +
            File.separator + "task-tracker" + File.separator + "tasks.json";
    private static final Map<Integer, Task> tasks = new HashMap<>();

    {
        initializeFile();
        loadFromJsonFile();
    }

    private String convertTasksToJson() {
        return tasks.values().stream()
                .map(this::toJson)
                .collect(Collectors.joining(",", "[", "]"));
    }

    private void writeJsonToFile(String json) {
        try(FileWriter fileWriter = new FileWriter(FILE_PATH)) {
            fileWriter.write(json);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void saveTasks() {
        String json = convertTasksToJson();
        writeJsonToFile(json);
    }

    public void updateTask(int id, String description) {
        Task task = tasks.get(id);
        if (task != null) {
            task.setDescription(description);
            task.setUpdatedAt(LocalDateTime.now());
            saveTasks();
        } else {
            System.out.println("Task with ID: " + id + " not found!");
        }
    }

    public void delete(int id) {
        if (tasks.remove(id) != null) {
            saveTasks();
        } else {
            System.out.println("Task with id: " + id + " not found!");
        }
    }

    public void loadFromJsonFile() {
        try {
            String json = new String(Files.readAllBytes(Paths.get(FILE_PATH)));

            if (!json.trim().equals("[]") || !json.isEmpty()) {
                String content = json.substring(1, json.length() - 1);
                String[] taskObjects = content.split("\\},\\s*\\{");

                for (String taskJson : taskObjects) {
                    String formattedJson = "{" + taskJson + "}";

                    Task task = fromJson(formattedJson);
                    tasks.put(task.getId(), task);
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error parsing the JSON: " + e.getMessage());
        }
    }

    public void loadByStatus(String status) {
        Status taskStatus = null;
        if (!status.isEmpty()) taskStatus = Status.valueOf(status.toUpperCase());

        if (taskStatus == null) {
            tasks.values().forEach(System.out::println);
        } else {
            Status finalTaskStatus = taskStatus;
            tasks.values().stream()
                    .filter(task -> task.getStatus() == finalTaskStatus)
                    .forEach(System.out::println);
        }
    }

    public void markByStatus(int id, Status status) {
        Task task = tasks.get(id);
        if (task != null) {
            task.setStatus(status);
            task.setUpdatedAt(LocalDateTime.now());
            saveTasks();
        } else {
            System.out.println("Couldn't find the task with the id: " + id);
        }
    }

    public void initializeFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                File parentDir = file.getParentFile();
                if (parentDir != null) {
                    parentDir.mkdirs();
                }
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating file: " + e.getMessage());
            }
        }
    }

    public void newTask(String description) {
        Task task = new Task(description);
        tasks.put(task.getId(), task);
        saveTasks();
    }

    public void printUsage() {
        System.out.println("""
                task-cli add \\"<task_description>\\"  - Add a new task
                task-cli list                       - List all tasks
                task-cli update <id> <new description>         - Updates the tasks where id = ?
                """);
    }

    public Optional<Integer> checkIfNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(Integer.parseInt(str));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public String toJson(Task task) {
        return "{" +
                "\"id\": \"" + task.getId() + "\"," +
                "\"description\": \"" + task.getDescription() + "\"," +
                "\"status\": \"" + task.getStatus() + "\"," +
                "\"createdAt\": \"" + task.getCreatedAt()+ "\"," +
                "\"updatedAt\": \"" + task.getUpdatedAt() + "\"" +
                "}";
    }

    public static Task fromJson(String json) {
        String[] parts = json.replace("{", "")
                .replace("}", "")
                .split(",");
        int id = Integer.parseInt(parts[0].split(":")[1].replace("\"", "").trim());
        String description = parts[1].split(":")[1].replace("\"", "").trim();
        String currentStatus = parts[2].split(":")[1].replace("\"", "").trim();
        LocalDateTime createdAt = parseDateTime(parts[3].split(":")[1].replace("\"", "").trim());
        LocalDateTime updatedAt = parseDateTime((parts[4].split(":")[1].replace("\"", "").trim()));
        return new Task(id, description, currentStatus, createdAt, updatedAt);
    }

    private static LocalDateTime parseDateTime(String dateTime) {
        try {
            return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (Exception e) {
            if (dateTime.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}")) {
                dateTime += ":00:00";
                return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } else {
                throw new IllegalArgumentException("Invalid date-time format: " + dateTime);
            }
        }
    }

}
