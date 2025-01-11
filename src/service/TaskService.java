package service;

import entity.Task;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TaskService {

    public static String FILE_PATH = "/Users/misu/IdeaProjects/task-tracker/src/resources/tasks.json";

    public static void saveToJsonFile(List<Task> tasks) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < tasks.size(); i++) {
            Task.currentId = i + 1;
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
        if (tasks.size() < id) {
            System.out.println("You entered the wrong id");
            return;
        }

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

}
