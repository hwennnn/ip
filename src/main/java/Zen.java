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
            } else {
                // Add the task to the array
                if (taskCount < 100) {
                    tasks[taskCount] = new Task(input);
                    taskCount++;
                    System.out.println(" added: " + input);
                } else {
                    System.out.println(" Sorry, you have reached the maximum of 100 tasks!");
                }
                System.out.println("____________________________________________________________");
            }
        }
        
        scanner.close();
    }
}
