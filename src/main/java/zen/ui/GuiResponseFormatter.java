package zen.ui;

import java.util.ArrayList;

import zen.task.Task;

/**
 * Handles formatting of responses specifically for the GUI interface
 */
public class GuiResponseFormatter {

    /**
     * Formats the task list for GUI display
     */
    public static String formatTaskList(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            return "No tasks in your list yet!";
        } else {
            StringBuilder output = new StringBuilder("Here are the tasks in your list:\n");
            for (int i = 0; i < tasks.size(); i++) {
                output.append(i + 1).append(".").append(tasks.get(i)).append("\n");
            }
            return output.toString().trim();
        }
    }

    /**
     * Formats matching tasks from a find operation for GUI display
     */
    public static String formatMatchingTasks(ArrayList<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            return "No matching tasks found in your list.";
        } else {
            StringBuilder output = new StringBuilder("Here are the matching tasks in your list:\n");
            for (int i = 0; i < matchingTasks.size(); i++) {
                output.append(i + 1).append(".").append(matchingTasks.get(i)).append("\n");
            }
            return output.toString().trim();
        }
    }

    /**
     * Formats task marked message for GUI display
     */
    public static String formatTaskMarked(Task task) {
        return "Nice! I've marked this task as done:\n   " + task;
    }

    /**
     * Formats task unmarked message for GUI display
     */
    public static String formatTaskUnmarked(Task task) {
        return "OK, I've marked this task as not done yet:\n   " + task;
    }

    /**
     * Formats task added message for GUI display
     */
    public static String formatTaskAdded(Task task, int totalTasks) {
        return "Got it. I've added this task:\n   " + task
               + "\nNow you have " + totalTasks + " tasks in the list.";
    }

    /**
     * Formats task deleted message for GUI display
     */
    public static String formatTaskDeleted(Task task, int totalTasks) {
        return "Noted. I've removed this task:\n   " + task
               + "\nNow you have " + totalTasks + " tasks in the list.";
    }

    /**
     * Formats goodbye message for GUI display
     */
    public static String formatGoodbye() {
        return "Bye. Hope to see you again soon!";
    }

    /**
     * Formats help message for GUI display
     */
    public static String formatHelp() {
        StringBuilder help = new StringBuilder();
        help.append("Here are the commands you can use:\n\n");
        help.append("bye                              - Exit the application\n");
        help.append("list                             - Show all tasks\n");
        help.append("help                             - Show this help message\n\n");
        help.append("todo <description>               - Add a todo task\n");
        help.append("deadline <description> /by <date> - Add a deadline task\n");
        help.append("event <description> /from <start> /to <end> - Add an event task\n\n");
        help.append("mark <number>                    - Mark a task as done\n");
        help.append("unmark <number>                  - Mark a task as not done\n");
        help.append("delete <number>                  - Delete a task\n\n");
        help.append("find <keyword>                   - Find tasks containing keyword\n\n");
        help.append("Examples:\n");
        help.append("  todo Buy groceries\n");
        help.append("  deadline Submit report /by 2024-12-25\n");
        help.append("  event Team meeting /from 2024-12-20 2pm /to 4pm\n");
        help.append("  mark 1\n");
        help.append("  find meeting");
        return help.toString();
    }

    /**
     * Formats error message for GUI display
     */
    public static String formatError(String message) {
        return "NOOOOO!!! " + message;
    }
}
