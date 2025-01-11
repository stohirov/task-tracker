import entity.Task;
import service.TaskService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        initializeFile();

        if (args.length == 0) {

            System.out.println("Usage: task-cli <command> <parameters>");
            return;

        }
        List<Task> tasks;
        String command = args[0];
        switch (command) {
            case "add":
                if (args.length < 2) {
                    printUsage();
                }
                tasks = TaskService.loadFromJsonFile();
                tasks.add(new Task(args[1]));
                TaskService.saveToJsonFile(tasks);
                break;
            case "list":
                List<Task> loadedTasks = TaskService.loadFromJsonFile();
                if (loadedTasks.isEmpty()) {
                    System.out.println("No tasks found (empty array).");
                    return;
                }
                for (Task task: TaskService.loadFromJsonFile()) {
                    System.out.println(task);
                }
                break;
            case "update":
                if (args .length < 2) {
                    printUsage();
                    return;
                }
                TaskService.updateTask(Integer.parseInt(args[1]), args[2]);
                break;
            case "delete":
                if (args.length < 2) {
                    printUsage();
                }
                TaskService.delete(Integer.parseInt(args[1]));
                break;
        }

    }

    private static void initializeFile() {
        File file = new File(TaskService.FILE_PATH);
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

    public static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  task-cli add \"<task_description>\"  - Add a new task");
        System.out.println("  task-cli list                       - List all tasks");
        System.out.println("  task-cli update <id> <new description>         - Updates the tasks where id = ?");
    }
}
