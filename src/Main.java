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
        List<Task> tasks = new ArrayList<>();
        String command = args[0];
        switch (command) {
            case "add":
                tasks.add(new Task(args[1]));
                TaskService.saveToJsonFile(tasks);
                break;
            case "list":
                List<Task> loadedTasks = TaskService.loadFromJsonFile();
                if (loadedTasks.isEmpty()) {
                    System.out.println("Did you add some tasks? cus I see nothing!");
                    return;
                }
                for (Task task: TaskService.loadFromJsonFile()) {
                    System.out.println(task);
                }
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
}
