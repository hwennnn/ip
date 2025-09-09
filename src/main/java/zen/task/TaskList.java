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
        assert tasks != null : "Tasks list should not be null";
        this.tasks = new ArrayList<>(tasks);
        assert this.tasks != null : "Internal tasks list should be properly initialized";
    }

    /**
     * Adds a task to the list
     *
     * @param task the task to add
     */
    public void addTask(Task task) {
        assert task != null : "Task to be added should not be null";
        assert tasks != null : "Tasks list should be initialized before adding tasks";
        int sizeBefore = tasks.size();
        tasks.add(task);
        assert tasks.size() == sizeBefore + 1 : "Task list size should increase by 1 after adding a task";
    }

    /**
     * Removes a task from the list
     *
     * @param index the index of the task to remove (0-based)
     * @return the removed task
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public Task deleteTask(int index) {
        assert tasks != null : "Tasks list should be initialized before deleting tasks";
        if (index < 0 || index >= tasks.size()) {
            throw new IndexOutOfBoundsException("Task index is out of range!");
        }
        int sizeBefore = tasks.size();
        Task removedTask = tasks.remove(index);
        assert removedTask != null : "Removed task should not be null for valid index";
        assert tasks.size() == sizeBefore - 1 : "Task list size should decrease by 1 after removing a task";
        return removedTask;
    }

    /**
     * Marks a task as done
     *
     * @param index the index of the task to mark (0-based)
     * @return the marked task
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public Task markTask(int index) {
        assert tasks != null : "Tasks list should be initialized before marking tasks";
        if (index < 0 || index >= tasks.size()) {
            throw new IndexOutOfBoundsException("Task index is out of range!");
        }
        Task task = tasks.get(index);
        assert task != null : "Task at valid index should not be null";
        task.markAsDone();
        assert task.isDone() : "Task should be marked as done after calling markAsDone()";
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
        assert tasks != null : "Tasks list should be initialized before unmarking tasks";
        if (index < 0 || index >= tasks.size()) {
            throw new IndexOutOfBoundsException("Task index is out of range!");
        }
        Task task = tasks.get(index);
        assert task != null : "Task at valid index should not be null";
        task.markAsNotDone();
        assert !task.isDone() : "Task should be marked as not done after calling markAsNotDone()";
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
        assert keyword != null : "Search keyword should not be null";
        assert tasks != null : "Tasks list should be initialized before searching";

        ArrayList<Task> matchingTasks = new ArrayList<>();
        String lowercaseKeyword = keyword.toLowerCase();

        for (Task task : tasks) {
            String taskDescription = task.getDescription().toLowerCase();
            if (taskDescription.contains(lowercaseKeyword)) {
                assert task.getDescription() != null : "Task description should not be null";
                if (task.getDescription().toLowerCase().contains(lowercaseKeyword)) {
                    matchingTasks.add(task);
                }
            }
        }

        return matchingTasks;
    }
}
