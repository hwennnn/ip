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
        return "Got it. I've added this task:\n   " + task + 
               "\nNow you have " + totalTasks + " tasks in the list.";
    }
    
    /**
     * Formats task deleted message for GUI display
     */
    public static String formatTaskDeleted(Task task, int totalTasks) {
        return "Noted. I've removed this task:\n   " + task + 
               "\nNow you have " + totalTasks + " tasks in the list.";
    }
    
    /**
     * Formats goodbye message for GUI display
     */
    public static String formatGoodbye() {
        return "Bye. Hope to see you again soon!";
    }
    
    /**
     * Formats error message for GUI display
     */
    public static String formatError(String message) {
        return "NOOOOO!!! " + message;
    }
}
