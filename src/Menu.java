import Enums.TaskStatus;
import Models.TaskModel;
import Models.TaskModelDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Menu {

    private final JsonFile jsonFile = new JsonFile();
    private final Scanner sc = new Scanner(System.in);
    private final String JSON_FILE_NAME = "data.json";

    public void printMenu() {
        boolean iterate = true;

        while (iterate) {

            System.out.println("""
                Please Select an option:
                1. Add task
                2. Mark task as done or in progress
                3. List all tasks
                4. List all 'not done' tasks
                5. List all 'done' tasks
                6. List all 'in progress' tasks
                0. Exit
                """);
            try {
                int option = Integer.parseInt(sc.nextLine());

                switch (option) {
                    case 1:
                        addTask();
                        break;
                    case 2:
                        setTaskStatus();
                        break;
                    case 3:
                        listAllTasks();
                        break;
                    case 4:
                        listNotDoneTasks();
                        break;
                    case 5:
                        listDoneTasks();
                        break;
                    case 6:
                        listInProgressTasks();
                        break;

                    case 0:
                        System.out.println("***Thanks for using Task Tracker App***");
                        iterate = false;
                        break;
                    default:
                        System.out.println("invalid option");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid option");
            }
        }


    }

    public void addTask() {
        String id = UUID.randomUUID().toString();
        System.out.println("Add task description");
        String description = sc.nextLine();
        String status = TaskStatus.TODO.getValue();
        String createdAt = LocalDateTime.now().toString();
        String updatedAt = LocalDateTime.now().toString();

        TaskModel newTask = new TaskModel(id, description, status, createdAt, updatedAt);
        jsonFile.createJsonFile(newTask);
    }

    public void setTaskStatus() {
        // Encontrar la tarea dentro del archivo data.json usando el id
        List<TaskModel> tasks = jsonFile.getTasksFromFile(JSON_FILE_NAME);
        listAllTasks();
        System.out.println("enter the first eight characters of the task id: ");
        String taskId = sc.nextLine();

        System.out.println("""
            *********************************************
            To which status you want to change the task:
            1. TODO
            2. Done
            3. In progress
            *********************************************""");

        try {
            int taskStatus = Integer.parseInt(sc.nextLine());
            boolean taskFound= false;
            for (int i = 0; i < tasks.size(); i++) {
                // get task by its position in the array
                TaskModel task = tasks.get(i);

                if (task.id().split("-")[0].equals(taskId)) {
                    taskFound = true;
                    // get actual date
                    String actualDate = LocalDateTime.now().toString();

                    // change task status
                    String newStatus = switch (taskStatus) {
                        case 1 -> TaskStatus.TODO.getValue();
                        case 2 -> TaskStatus.DONE.getValue();
                        case 3 -> TaskStatus.IN_PROGRESS.getValue();
                        default -> throw new IllegalStateException("Unexpected value: " + taskStatus);
                    };

                    // Create new task
                    TaskModel updatedTask = new TaskModel(task.id(), task.description(), newStatus, task.createdAt(), actualDate);
                    // Replace task with updated one
                    tasks.set(i, updatedTask);

                    // convert task array into tasksDTO
                    TaskModelDTO taskModelDTO = new TaskModelDTO(tasks);

                    // write new task
                    jsonFile.writeTasksToFile(taskModelDTO, JSON_FILE_NAME);

                    System.out.println("***Task updated***");
                    break;
                }

            }

            if(!taskFound){
                System.out.println("Task not found");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid option");
        }

    }

    public void listAllTasks() {
        List<TaskModel> tasks = jsonFile.getTasksFromFile(JSON_FILE_NAME);
        System.out.println("*************** All Tasks ***************");
        for (TaskModel task : tasks) {
            System.out.printf("""
                - id: %s
                - Description: %s
                - Status: %s
                - Created At: %s
                - Updated At: %s
                ***********************
                """, task.id(), task.description(), task.status(), task.createdAt(), task.updatedAt());
        }

    }

    public void listNotDoneTasks() {
        List<TaskModel> tasks = jsonFile.getTasksFromFile(JSON_FILE_NAME);
        System.out.println("*************** Not Done Tasks ***************");

        for (TaskModel task : tasks) {
            if (task.status().equals(TaskStatus.TODO.getValue()) || task.status().equals(TaskStatus.IN_PROGRESS.getValue())) {
                System.out.printf("""
                    - id: %s
                    - Description: %s
                    - Status: %s
                    - Created At: %s
                    - Updated At: %s
                    ***********************
                    """, task.id(), task.description(), task.status(), task.createdAt(), task.updatedAt());
            }
        }
    }

    private void listDoneTasks() {
        List<TaskModel> tasks = jsonFile.getTasksFromFile(JSON_FILE_NAME);
        System.out.println("*************** Done Tasks ***************");
        for (TaskModel task : tasks) {
            if (task.status().equals(TaskStatus.DONE.getValue())) {
                System.out.printf("""
                    - id: %s
                    - Description: %s
                    - Status: %s
                    - Created At: %s
                    - Updated At: %s
                    ***********************
                    """, task.id(), task.description(), task.status(), task.createdAt(), task.updatedAt());
            }
        }

    }

    private void listInProgressTasks() {
        List<TaskModel> tasks = jsonFile.getTasksFromFile(JSON_FILE_NAME);
        System.out.println("*************** In Progress Tasks ***************");

        for (TaskModel task : tasks) {
            if (task.status().equals(TaskStatus.IN_PROGRESS.getValue())) {
                System.out.printf("""
                    - id: %s
                    - Description: %s
                    - Status: %s
                    - Created At: %s
                    - Updated At: %s
                    ***********************
                    """, task.id(), task.description(), task.status(), task.createdAt(), task.updatedAt());
            }
        }
    }

}
