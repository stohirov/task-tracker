import entity.Status;
import service.TaskService;

import java.util.Optional;

public class Main {

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Usage: java Main <command> <parameters>");
            TaskService.printUsage();
            return;

        }

        String command = args[0];
        switch (command) {
            case "add" -> handleAddCommand(args);
            case "update" -> handleUpdateCommand(args);
            case "delete" -> handleDeleteCommand(args);
            case "list" -> handleListCommand(args);
            case "mark-in-progress" -> handleMarkCommands(args, Status.IN_PROGRESS);
            case "mark-done" -> handleMarkCommands(args, Status.DONE);
            default -> {
                System.out.println("Unknown command!");
                TaskService.printUsage();
            }
        }

    }

    private static void handleListCommand(String[] args) {
        String status = args.length > 1 ? args[1] : "";
        TaskService.loadByStatus(status);
    }

    private static void handleMarkCommands(String[] args, Status status) {
        if (args.length < 2) {
            System.out.println("Not enough arguments!");
            return;
        }
        Optional<Integer> id = TaskService.isNumeric(args[1]);
        if (id.isPresent()) TaskService.markByStatus(id.get(), status);
        else System.out.println("Input mismatch error!");
    }

    private static void handleDeleteCommand(String[] args) {
        if (args.length < 2) {
            TaskService.printUsage();
            return;
        }
        Optional<Integer> id = TaskService.isNumeric(args[1]);
        if (id.isPresent()) {
            TaskService.delete(id.get());
        } else {
            System.out.println("Input mismatch error!");
        }
    }

    private static void handleUpdateCommand(String[] args) {
        if (args.length < 3) {
            TaskService.printUsage();
            return;
        }
        Optional<Integer> id = TaskService.isNumeric(args[1]);
        if (id.isEmpty()) {
            TaskService.printUsage();
        } else {
            TaskService.updateTask(id.get(), args[2]);
        }
    }

    private static void handleAddCommand(String[] args) {
        if (args.length < 2) {
            TaskService.printUsage();
            return;
        }

        TaskService.newTask(args[1]);
        System.out.println("Task added successfully!");
    }

}
