import java.util.ArrayList;
import java.util.Scanner;

public class Fenix implements SampleStrings {
    
    public static ArrayList<Task> taskArrayList = new ArrayList<>();
    private final Scanner scanner;

    // Constructor
    public Fenix() {
        this.scanner = new Scanner(System.in);
    }

    public void greet() {
        System.out.println(HORIZONTAL_LINE_USER_COMMAND);
        System.out.println(GREETING);
        System.out.println(SERVICE_PROMPT);
    }

    public void acceptUserInput() {
        System.out.println(HORIZONTAL_LINE_USER_COMMAND);
        String userInput = getUserInput();
        processUserInput(userInput);
    }

    private String getUserInput() {
        return scanner.nextLine();
    }

    private void processUserInput(String userInput) {
        String[] words = userInput.trim().split(" ", 2);
        String commandType = words[0];
        String commandInfo = ((words.length > 1) ? words[1] : "") ;
        switch (commandType) {
        case "bye":
            bidFarewell();
            return;
        case "list":
            showAllTasks(false);
            break;
        case "mark":
            markAsDone(commandInfo);
            break;
        case "unmark":
            unmarkAsDone(commandInfo);
            break;
        case "todo":
        case "deadline":
        case "event":
            processTasks(commandType, commandInfo);
            break;
        case "delete":
            deleteTask(commandInfo);
            break;
        default:
            System.out.println("Please provide a valid command");
        }
        acceptUserInput();
    }

    public void bidFarewell() {
        System.out.println(FAREWELL);
        System.out.println(HORIZONTAL_LINE_USER_COMMAND);
        scanner.close();
    }

    public void showAllTasks(boolean isModified) {
        String extraSpace = (isModified ? "\t" : "");
        String space = extraSpace + "\t";
        for (Task task : taskArrayList) {
            String index = (taskArrayList.indexOf(task) + 1) + ". ";
            System.out.println(space + index + task);
        }
    }

    private void markAsDone(String taskNumber) {
        int taskIndex = getTaskIndex(taskNumber);
        if (taskIndex == -1)
        {
            return;
        }
        markTaskAsDone(taskIndex);
    }

    private int getTaskIndex(String taskNumber) {
        if (taskNumber.isBlank()) {
            System.out.println("Please provide a task");
            return -1;
        }
        if (!isValidTaskNumber(taskNumber)) {
            System.out.println("Please provide a valid task number");
            return -1;
        }
        return Integer.parseInt(taskNumber) - 1;
    }

    private boolean isValidTaskNumber(String input) {
        try {
            int index = Integer.parseInt(input);
            return index > 0 && index <= taskArrayList.size();
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void markTaskAsDone(int taskNumber) {
        System.out.println("Task successfully completed. A job well executed.");
        taskArrayList.get(taskNumber).markAsDone();
        printListTaskStatusChange();
    }

    private void unmarkAsDone(String taskNumber) {
        int taskIndex = getTaskIndex(taskNumber);
        if (taskIndex == -1)
        {
            return;
        }
        unmarkTaskAsDone(taskIndex);
    }

    public void unmarkTaskAsDone(int taskNumber) {
        System.out.println("Understood. This task has been marked as not done yet.");
        taskArrayList.get(taskNumber).unmarkAsDone();
        printListTaskStatusChange();
    }

    private void printListTaskStatusChange() {
        System.out.println(HORIZONTAL_LINE_FENIX_MODIFICATION);
        showAllTasks(true);
        System.out.println(HORIZONTAL_LINE_FENIX_MODIFICATION);
    }

    public void processTasks(String commandType, String commandInfo) {
        if (commandType == null || commandType.isBlank()) {
            System.out.println("Please provide a command");
            return;
        } else if (commandInfo == null || commandInfo.isBlank()) {
            System.out.println("Please provide a task");
            return;
        }
        Task task = returnTaskObject(commandType, commandInfo);
        if (task == null) {
            return;
        }
        storeTask(task);
        printFenixModification(ADD, task);
    }

    private static void printFenixModification(String modification, Task task) {
        System.out.println(HORIZONTAL_LINE_FENIX_MODIFICATION);
        System.out.println("\t\t" + modification + task);
        System.out.println(HORIZONTAL_LINE_FENIX_MODIFICATION);
        System.out.println("You now have " + taskArrayList.size() + " tasks awaiting your attention.");
    }

    private Task returnTaskObject(String type, String information) {
        switch (type) {
        case "todo":
            return new Todo(information);
        case "deadline":
            try {
                return new Deadline(information);
            } catch (IllegalArgumentException | FenixException e) {
                System.out.println(e.getMessage());
                return null;
            }
        case "event":
            try {
                return new Event(information);
            } catch (IllegalArgumentException | FenixException e) {
                System.out.println(e.getMessage());
                return null;
            }
        default:
            System.out.println("Please provide a valid command for the task type");
            return null;
        }
    }

    public void storeTask(Task task) {
        taskArrayList.add(task);
    }

    public void deleteTask(String taskNumber) {
        int taskIndex = getTaskIndex(taskNumber);
        if (taskIndex == -1)
        {
            return;
        }
        Task task = taskArrayList.get(taskIndex);
        taskArrayList.remove(taskIndex);
        printFenixModification(DELETE, task);
    }
}