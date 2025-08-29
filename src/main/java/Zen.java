import java.util.ArrayList;
import java.util.Scanner;

public class Zen {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskStorage storage = new TaskStorage();
        ArrayList<Task> tasks = storage.loadTasks();
        
        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm Zen");
        System.out.println(" What can I do for you?");
        System.out.println("____________________________________________________________");
        
        while (true) {
            String input = scanner.nextLine().trim();
            
            System.out.println("____________________________________________________________");
            
            if (input.equals("bye")) {
                System.out.println(" Bye. Hope to see you again soon!");
                System.out.println("____________________________________________________________");
                break;
            } else if (input.equals("list")) {
                if (tasks.isEmpty()) {
                    System.out.println(" No tasks in your list yet!");
                } else {
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println(" " + (i + 1) + "." + tasks.get(i));
                    }
                }
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("mark ")) {
                try {
                    int taskIndex = Integer.parseInt(input.substring(5)) - 1;
                    if (taskIndex >= 0 && taskIndex < tasks.size()) {
                        tasks.get(taskIndex).markAsDone();
                        storage.saveTasks(tasks);
                        System.out.println(" Nice! I've marked this task as done:");
                        System.out.println("   " + tasks.get(taskIndex));
                    } else {
                        System.out.println(" Task number is out of range!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println(" Please provide a valid task number!");
                }
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("unmark ")) {
                try {
                    int taskIndex = Integer.parseInt(input.substring(7)) - 1;
                    if (taskIndex >= 0 && taskIndex < tasks.size()) {
                        tasks.get(taskIndex).markAsNotDone();
                        storage.saveTasks(tasks);
                        System.out.println(" OK, I've marked this task as not done yet:");
                        System.out.println("   " + tasks.get(taskIndex));
                    } else {
                        System.out.println(" Task number is out of range!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println(" Please provide a valid task number!");
                }
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("delete ")) {
                try {
                    int taskIndex = Integer.parseInt(input.substring(7)) - 1;
                    if (taskIndex >= 0 && taskIndex < tasks.size()) {
                        Task removedTask = tasks.remove(taskIndex);
                        storage.saveTasks(tasks);
                        System.out.println(" Noted. I've removed this task:");
                        System.out.println("   " + removedTask);
                        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    } else {
                        System.out.println(" Task number is out of range!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println(" Please provide a valid task number!");
                }
                System.out.println("____________________________________________________________");
            } else if (input.equals("todo")) {
                System.out.println(" NOOOO!!! The description of a todo cannot be empty.");
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("todo ")) {
                String description = input.substring(5).trim();
                try {
                    tasks.add(new Todo(description));
                    storage.saveTasks(tasks);
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + tasks.get(tasks.size() - 1));
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                } catch (ZenException e) {
                    System.out.println(" NOOOO!!! " + e.getMessage());
                }
                System.out.println("____________________________________________________________");
            } else if (input.equals("deadline")) {
                System.out.println(" NOOOO!!! Please use the format: deadline <description> /by <date>");
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("deadline ")) {
                String remaining = input.substring(9).trim();
                int byIndex = remaining.indexOf(" /by ");
                if (byIndex != -1 && byIndex + 5 < remaining.length()) {
                    String description = remaining.substring(0, byIndex).trim();
                    String by = remaining.substring(byIndex + 5).trim();
                    try {
                        tasks.add(new Deadline(description, by));
                        storage.saveTasks(tasks);
                        System.out.println(" Got it. I've added this task:");
                        System.out.println("   " + tasks.get(tasks.size() - 1));
                        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    } catch (ZenException e) {
                        System.out.println(" NOOOOO!!! " + e.getMessage());
                    }
                } else {
                    System.out.println(" NOOOOO!!! Please use the format: deadline <description> /by <date>");
                }
                System.out.println("____________________________________________________________");
            } else if (input.equals("event")) {
                System.out.println(" NOOOOO!!! Please use the format: event <description> /from <start> /to <end>");
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("event ")) {
                String remaining = input.substring(6).trim();
                int fromIndex = remaining.indexOf(" /from ");
                int toIndex = remaining.indexOf(" /to ");
                if (fromIndex != -1 && toIndex != -1 && fromIndex < toIndex && toIndex + 5 < remaining.length()) {
                    String description = remaining.substring(0, fromIndex).trim();
                    String from = remaining.substring(fromIndex + 7, toIndex).trim();
                    String to = remaining.substring(toIndex + 5).trim();
                    try {
                        tasks.add(new Event(description, from, to));
                        storage.saveTasks(tasks);
                        System.out.println(" Got it. I've added this task:");
                        System.out.println("   " + tasks.get(tasks.size() - 1));
                        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    } catch (ZenException e) {
                        System.out.println(" NOOOOO!!! " + e.getMessage());
                    }
                } else {
                    System.out.println(" NOOOOO!!! Please use the format: event <description> /from <start> /to <end>");
                }
                System.out.println("____________________________________________________________");
            } else {
                // Handle unknown commands and empty input
                if (input.isEmpty()) {
                    System.out.println(" NOOOOO!!! Please enter a command.");
                } else {
                    System.out.println(" NOOOOO!!! I'm sorry, but I don't know what that means :-(");
                }
                System.out.println("____________________________________________________________");
            }
        }
        
        scanner.close();
    }
}
