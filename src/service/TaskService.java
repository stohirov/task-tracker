package service;

import entity.Task;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskService {

    public static String FILE_PATH = "/Users/misu/IdeaProjects/task-tracker/src/resources/tasks.json";

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



    public static List<Task> loadFromJsonFile() {
        List<Task> tasks = new ArrayList<>();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(TaskService.FILE_PATH))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                json.append(line);
            }

            String[] objects = json.toString().replace("[", "").replace("]", "").split("\\}, \\}");
            for (String obj: objects) {
                String formattedObj = obj.startsWith("{") ? obj : "{" + obj;
                formattedObj = formattedObj.endsWith("}") ? formattedObj : formattedObj + "}";
                tasks.add(Task.fromJson(formattedObj));
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return tasks;
    }

}
