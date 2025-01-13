import entity.Status;
import entity.Task;
import service.TaskService;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        TaskService.initializeFile();

        if (args.length == 0) {

            System.out.println("Usage: java Main <command> <parameters>");
            return;

        }
        List<Task> tasks;
        String command = args[0];
        switch (command) {
            case "add":
                if (args.length < 2) {
                    TaskService.printUsage();
                    return;
                }
                tasks = TaskService.loadFromJsonFile();
                tasks.add(TaskService.newTask(args[1]));
                TaskService.saveToJsonFile(tasks);
                break;
            case "list":
                String status = "";
                if (args.length == 2) {
                    status = args[1];
                }
                TaskService.loadByStatus(status);
                break;
            case "update":
                if (args .length < 2) {
                    TaskService.printUsage();
                    return;
                }
                if (TaskService.isNumeric(args[2])) TaskService.updateTask(Integer.parseInt(args[1]), args[2]);
                else System.out.println("Pls enter a number");

                break;
            case "delete":
                if (args.length < 2) {
                    TaskService.printUsage();
                    return;
                }
                if (TaskService.isNumeric(args[1])) TaskService.delete(Integer.parseInt(args[1]));
                else System.out.println("Pls enter a number");
                break;
            case "mark-in-progress":
                if (args.length > 1) {
                    if (!TaskService.isNumeric(args[1])) return;
                    int id = Integer.parseInt(args[1]);
                    TaskService.markByStatus(id, Status.IN_PROGRESS);
                }
                break;
            case "mark-done":
                if (args.length > 1) {
                    if (!TaskService.isNumeric(args[1])) return;
                    int id = Integer.parseInt(args[1]);
                    TaskService.markByStatus(id, Status.DONE);
                }
                break;
            default:
                TaskService.printUsage();
        }

    }

}
