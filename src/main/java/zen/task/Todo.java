package zen.task;

import zen.exception.ZenException;

/**
 * Represents a simple todo task without any specific deadline or time constraints.
 * This is the most basic type of task in the system.
 */
public class Todo extends Task {

    /**
     * Constructs a new Todo task with the specified description.
     *
     * @param description the description of the todo task
     * @throws ZenException if the description is null or empty
     */
    public Todo(String description) throws ZenException {
        super(description, TaskType.TODO);
    }

    @Override
    public String toString() {
        return super.toString();
    }
} 