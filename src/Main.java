import entity.Status;
import service.TaskService;

import java.util.Optional;

public class Main {

    private static final TaskService taskService = new TaskService();

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Usage: java Main <command> <parameters>");
            taskService.printUsage();
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
                taskService.printUsage();
            }
        }

    }

    private static void handleListCommand(String[] args) {
        String status = args.length > 1 ? args[1] : "";
        taskService.loadByStatus(status);
    }

    private static void handleMarkCommands(String[] args, Status status) {
        if (args.length < 2) {
            System.out.println("Not enough arguments!");
            return;
        }
        Optional<Integer> id = taskService.isNumeric(args[1]);
        if (id.isPresent()) taskService.markByStatus(id.get(), status);
        else System.out.println("Input mismatch error!");
    }

    private static void handleDeleteCommand(String[] args) {
        if (args.length < 2) {
            taskService.printUsage();
            return;
        }
        Optional<Integer> id = taskService.isNumeric(args[1]);
        if (id.isPresent()) {
            taskService.delete(id.get());
        } else {
            System.out.println("Input mismatch error!");
        }
    }

    private static void handleUpdateCommand(String[] args) {
        if (args.length < 3) {
            taskService.printUsage();
            return;
        }
        Optional<Integer> id = taskService.isNumeric(args[1]);
        if (id.isEmpty()) {
            taskService.printUsage();
        } else {
            taskService.updateTask(id.get(), args[2]);
        }
    }

    private static void handleAddCommand(String[] args) {
        if (args.length < 2) {
            taskService.printUsage();
            return;
        }

        taskService.newTask(args[1]);
        System.out.println("Task added successfully!");
    }

}
