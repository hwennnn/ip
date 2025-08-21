import java.util.Scanner;

public class Zen {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[100];
        int taskCount = 0;
        
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
                if (taskCount == 0) {
                    System.out.println(" No tasks in your list yet!");
                } else {
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println(" " + (i + 1) + "." + tasks[i]);
                    }
                }
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("mark ")) {
                try {
                    int taskIndex = Integer.parseInt(input.substring(5)) - 1;
                    if (taskIndex >= 0 && taskIndex < taskCount) {
                        tasks[taskIndex].markAsDone();
                        System.out.println(" Nice! I've marked this task as done:");
                        System.out.println("   " + tasks[taskIndex]);
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
                    if (taskIndex >= 0 && taskIndex < taskCount) {
                        tasks[taskIndex].markAsNotDone();
                        System.out.println(" OK, I've marked this task as not done yet:");
                        System.out.println("   " + tasks[taskIndex]);
                    } else {
                        System.out.println(" Task number is out of range!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println(" Please provide a valid task number!");
                }
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("todo ")) {
                String description = input.substring(5);
                if (taskCount < 100) {
                    tasks[taskCount] = new Todo(description);
                    taskCount++;
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + tasks[taskCount - 1]);
                    System.out.println(" Now you have " + taskCount + " tasks in the list.");
                } else {
                    System.out.println(" Sorry, you have reached the maximum of 100 tasks!");
                }
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("deadline ")) {
                String remaining = input.substring(9);
                int byIndex = remaining.indexOf(" /by ");
                if (byIndex != -1) {
                    String description = remaining.substring(0, byIndex);
                    String by = remaining.substring(byIndex + 5);
                    if (taskCount < 100) {
                        tasks[taskCount] = new Deadline(description, by);
                        taskCount++;
                        System.out.println(" Got it. I've added this task:");
                        System.out.println("   " + tasks[taskCount - 1]);
                        System.out.println(" Now you have " + taskCount + " tasks in the list.");
                    } else {
                        System.out.println(" Sorry, you have reached the maximum of 100 tasks!");
                    }
                } else {
                    System.out.println(" Please use the format: deadline <description> /by <date>");
                }
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("event ")) {
                String remaining = input.substring(6);
                int fromIndex = remaining.indexOf(" /from ");
                int toIndex = remaining.indexOf(" /to ");
                if (fromIndex != -1 && toIndex != -1) {
                    String description = remaining.substring(0, fromIndex);
                    String from = remaining.substring(fromIndex + 7, toIndex);
                    String to = remaining.substring(toIndex + 5);
                    if (taskCount < 100) {
                        tasks[taskCount] = new Event(description, from, to);
                        taskCount++;
                        System.out.println(" Got it. I've added this task:");
                        System.out.println("   " + tasks[taskCount - 1]);
                        System.out.println(" Now you have " + taskCount + " tasks in the list.");
                    } else {
                        System.out.println(" Sorry, you have reached the maximum of 100 tasks!");
                    }
                } else {
                    System.out.println(" Please use the format: event <description> /from <start> /to <end>");
                }
                System.out.println("____________________________________________________________");
            } else {
                // Default: treat as a todo task for backward compatibility
                if (taskCount < 100) {
                    tasks[taskCount] = new Todo(input);
                    taskCount++;
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + tasks[taskCount - 1]);
                    System.out.println(" Now you have " + taskCount + " tasks in the list.");
                } else {
                    System.out.println(" Sorry, you have reached the maximum of 100 tasks!");
                }
                System.out.println("____________________________________________________________");
            }
        }
        
        scanner.close();
    }
}
