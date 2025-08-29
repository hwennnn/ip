package zen.task;

import zen.exception.ZenException;

/**
 * Abstract base class representing a task in the task management system.
 * This class provides common functionality for all types of tasks including
 * description, completion status, and task type management.
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;
    protected TaskType taskType;

    /**
     * Constructs a new Task with the specified description and type.
     * 
     * @param description the description of the task (cannot be null or empty)
     * @param taskType the type of the task
     * @throws ZenException if the description is null or empty
     */
    public Task(String description, TaskType taskType) throws ZenException {
        if (description == null || description.trim().isEmpty()) {
            throw new ZenException("The description of a task cannot be empty.");
        }
        this.description = description.trim();
        this.isDone = false;
        this.taskType = taskType;
    }

    /**
     * Gets the status icon for this task.
     * 
     * @return "X" if the task is done, " " (space) if not done
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsNotDone() {
        this.isDone = false;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isDone() {
        return this.isDone;
    }

    public TaskType getTaskType() {
        return this.taskType;
    }

    @Override
    public String toString() {
        return taskType + "[" + getStatusIcon() + "] " + description;
    }
} 