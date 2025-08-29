package zen.ui;

import java.util.ArrayList;
import java.util.Scanner;

import zen.task.Task;

/**
 * Handles all user interface operations including input and output
 */
public class Ui {
    private static final String LINE_SEPARATOR = "____________________________________________________________";
    private Scanner scanner;

    /**
     * Constructs a new Ui instance and initializes the scanner for user input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Shows the welcome message when the application starts
     */
    public void showWelcome() {
        showLine();
        System.out.println(" Hello! I'm Zen");
        System.out.println(" What can I do for you?");
        showLine();
    }

    /**
     * Shows the goodbye message when the application exits
     */
    public void showGoodbye() {
        System.out.println(" Bye. Hope to see you again soon!");
        showLine();
    }

    /**
     * Shows the horizontal line separator
     */
    public void showLine() {
        System.out.println(LINE_SEPARATOR);
    }

    /**
     * Reads a command from the user
     *
     * @return the user input as a trimmed string
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Shows an error message
     *
     * @param message the error message to display
     */
    public void showError(String message) {
        System.out.println(" NOOOOO!!! " + message);
    }

    /**
     * Shows a loading error message
     */
    public void showLoadingError() {
        System.out.println(" Warning: Could not load tasks from file. Starting with empty task list.");
    }

    /**
     * Shows the list of tasks
     *
     * @param tasks the list of tasks to display
     */
    public void showTaskList(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println(" No tasks in your list yet!");
        } else {
            System.out.println(" Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println(" " + (i + 1) + "." + tasks.get(i));
            }
        }
    }

    /**
     * Shows the matching tasks from a find operation
     * @param matchingTasks the list of tasks that match the search keyword
     */
    public void showMatchingTasks(ArrayList<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            System.out.println(" No matching tasks found in your list.");
        } else {
            System.out.println(" Here are the matching tasks in your list:");
            for (int i = 0; i < matchingTasks.size(); i++) {
                System.out.println(" " + (i + 1) + "." + matchingTasks.get(i));
            }
        }
    }

    /**
     * Shows a task that has been marked as done
     *
     * @param task the task that was marked as done
     */
    public void showTaskMarked(Task task) {
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
    }

    /**
     * Shows a task that has been marked as not done
     *
     * @param task the task that was marked as not done
     */
    public void showTaskUnmarked(Task task) {
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
    }

    /**
     * Shows a task that has been added
     *
     * @param task       the task that was added
     * @param totalTasks the total number of tasks after addition
     */
    public void showTaskAdded(Task task, int totalTasks) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + totalTasks + " tasks in the list.");
    }

    /**
     * Shows a task that has been deleted
     *
     * @param task       the task that was deleted
     * @param totalTasks the total number of tasks after deletion
     */
    public void showTaskDeleted(Task task, int totalTasks) {
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + totalTasks + " tasks in the list.");
    }

    /**
     * Closes the scanner when the application ends
     */
    public void close() {
        scanner.close();
    }
}
