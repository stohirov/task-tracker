package service;

import entity.Status;
import entity.Task;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TaskService {

    public static String FILE_PATH = System.getProperty("user.home") +
            File.separator + "task-tracker" + File.separator + "tasks.json";
    private static final Map<Integer, Task> tasks = new HashMap<>();

    static {
        initializeFile();
        loadFromJsonFile();
    }

    private static String convertTasksToJson() {
        return tasks.values().stream()
                .map(Task::toJson)
                .collect(Collectors.joining(",", "[", "]"));
    }

    private static void writeJsonToFile(String json) {
        try(FileWriter fileWriter = new FileWriter(FILE_PATH)) {
            fileWriter.write(json);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void saveTasks() {
        String json = convertTasksToJson();
        writeJsonToFile(json);
    }

    public static void updateTask(int id, String description) {
        Task task = tasks.get(id);
        if (task != null) {
            task.setDescription(description);
            task.setUpdatedAt(LocalDateTime.now());
            saveTasks();
        } else {
            System.out.println("Task with ID: " + id + " not found!");
        }
    }

    public static void delete(int id) {
        if (tasks.remove(id) != null) {
            saveTasks();
        } else {
            System.out.println("Task with id: " + id + " not found!");
        }
    }

    public static void loadFromJsonFile() {
        try {
            String json = new String(Files.readAllBytes(Paths.get(FILE_PATH)));

            if (!json.trim().equals("[]") || !json.isEmpty()) {
                String content = json.substring(1, json.length() - 1);
                String[] taskObjects = content.split("\\},\\s*\\{");

                for (String taskJson : taskObjects) {
                    String formattedJson = "{" + taskJson + "}";

                    Task task = Task.fromJson(formattedJson);
                    tasks.put(task.getId(), task);
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error parsing the JSON: " + e.getMessage());
        }
    }

    public static void loadByStatus(String status) {
        Status taskStatus = Status.valueOf("");
        try {
            taskStatus = Status.valueOf(status.toUpperCase());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        Status finalTaskStatus = taskStatus;
        tasks.values().stream()
                .filter(task -> task.getStatus() == finalTaskStatus)
                .forEach(System.out::println);
    }

    public static void markByStatus(int id, Status status) {
        Task task = tasks.get(id);
        if (task != null) {
            task.setStatus(status);
            task.setUpdatedAt(LocalDateTime.now());
            saveTasks();
        } else {
            System.out.println("Couldn't find the task with the id: " + id);
        }
    }

    public static void initializeFile() {
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

    public static Task newTask(String description) {
        Task task = new Task(description);
        tasks.put(task.getId(), task);
        saveTasks();
        return task;
    }

    public static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  task-cli add \"<task_description>\"  - Add a new task");
        System.out.println("  task-cli list                       - List all tasks");
        System.out.println("  task-cli update <id> <new description>         - Updates the tasks where id = ?");
    }

    public static Optional<Integer> isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(Integer.parseInt(str));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

}
