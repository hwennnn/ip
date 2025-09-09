package zen.task;

import java.util.ArrayList;

/**
 * Represents a list of tasks and provides operations to manage them
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Creates an empty task list
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list with the given tasks
     *
     * @param tasks initial list of tasks
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the list
     *
     * @param task the task to add
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Removes a task from the list
     *
     * @param index the index of the task to remove (0-based)
     * @return the removed task
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public Task deleteTask(int index) {
        if (index < 0 || index >= tasks.size()) {
            throw new IndexOutOfBoundsException("Task index is out of range!");
        }
        return tasks.remove(index);
    }

    /**
     * Marks a task as done
     *
     * @param index the index of the task to mark (0-based)
     * @return the marked task
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public Task markTask(int index) {
        if (index < 0 || index >= tasks.size()) {
            throw new IndexOutOfBoundsException("Task index is out of range!");
        }
        Task task = tasks.get(index);
        task.markAsDone();
        return task;
    }

    /**
     * Marks a task as not done
     *
     * @param index the index of the task to unmark (0-based)
     * @return the unmarked task
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public Task unmarkTask(int index) {
        if (index < 0 || index >= tasks.size()) {
            throw new IndexOutOfBoundsException("Task index is out of range!");
        }
        Task task = tasks.get(index);
        task.markAsNotDone();
        return task;
    }

    /**
     * Gets the task at the specified index
     *
     * @param index the index of the task (0-based)
     * @return the task at the specified index
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public Task getTask(int index) {
        if (index < 0 || index >= tasks.size()) {
            throw new IndexOutOfBoundsException("Task index is out of range!");
        }
        return tasks.get(index);
    }

    /**
     * Gets the number of tasks in the list
     *
     * @return the size of the task list
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Checks if the task list is empty
     *
     * @return true if the list is empty, false otherwise
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Gets the underlying ArrayList of tasks
     *
     * @return the ArrayList containing all tasks
     */
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    /**
     * Finds tasks that contain the specified keyword in their description
     *
     * @param keyword the keyword to search for
     * @return ArrayList of tasks that match the keyword (case-insensitive)
     */
    public ArrayList<Task> findTasksContaining(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        String lowercaseKeyword = keyword.toLowerCase();

        for (Task task : tasks) {
            String taskDescription = task.getDescription().toLowerCase();
            if (taskDescription.contains(lowercaseKeyword)) {
                matchingTasks.add(task);
            }
        }

        return matchingTasks;
    }
}
