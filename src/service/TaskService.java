package service;

import entity.Status;
import entity.Task;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TaskService {

    public static String FILE_PATH = "task-tracker/src/resources/tasks.json";

    public static void saveToJsonFile(List<Task> tasks) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < tasks.size(); i++) {
            json.append(tasks.get(i).toJson());
            if (i < tasks.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");
        try(FileWriter fileWriter = new FileWriter(TaskService.FILE_PATH)) {
            fileWriter.write(json.toString());
            System.out.println("Tasks saved successfully!");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void updateTask(int id, String description) {
        List<Task> tasks = loadFromJsonFile();
        for (Task task: tasks) {
            if (task.getId() == id) task.setDescription(description);
        }
        saveToJsonFile(tasks);
    }

    public static void delete(int id) {
        List<Task> tasks = loadFromJsonFile();
        tasks.removeIf(task -> task.getId() == id);
        cleanJsonFileIfEmpty();
        saveToJsonFile(tasks);
    }

    public static void cleanJsonFileIfEmpty() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            if (content.trim().equals("[]")) {
                try (FileWriter writer = new FileWriter(FILE_PATH)) {
                    writer.write("");
                }
            }
        } catch (IOException e) {
            System.err.println("Error handling the JSON file: " + e.getMessage());
        }
    }

    public static List<Task> loadFromJsonFile() {
        List<Task> tasks = new ArrayList<>();
        try {
            String json = new String(Files.readAllBytes(Paths.get(FILE_PATH)));

            if (json.trim().equals("[]") || json.isEmpty()) {
                return tasks;
            }

            String content = json.substring(1, json.length() - 1);
            String[] taskObjects = content.split("\\},\\s*\\{");

            for (String taskJson : taskObjects) {
                String formattedJson = "{" + taskJson + "}";

                Task task = Task.fromJson(formattedJson);
                tasks.add(task);
            }

        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error parsing the JSON: " + e.getMessage());
        }
        return tasks;
    }

    public static void loadByStatus(String status) {
        List<Task> tasks = loadFromJsonFile();
        switch (status) {
            case "done":
                for (Task task: tasks) {
                    if (task.getStatus() == Status.DONE) System.out.println(task);
                }
                break;
            case "in-progress":
                for (Task task: tasks) {
                    if (task.getStatus() == Status.IN_PROGRESS) System.out.println(task);
                }
                break;
            case "todo":
                for (Task task: tasks) {
                    if (task.getStatus() == Status.TODO) System.out.println(task);
                }
                break;
            default:
                for (Task task: tasks) {
                    System.out.println(task);
                }
        }
    }

    public static void markByStatus(int id, Status status) {
        List<Task> tasks = loadFromJsonFile();
        for (Task task: tasks) {
            if (task.getId() == id) task.setStatus(status);
        }
        saveToJsonFile(tasks);
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
        task.setId(Task.randomId());
        return task;
    }

    public static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  task-cli add \"<task_description>\"  - Add a new task");
        System.out.println("  task-cli list                       - List all tasks");
        System.out.println("  task-cli update <id> <new description>         - Updates the tasks where id = ?");
    }

    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
