package zen.task;

import zen.exception.ZenException;
import zen.util.FlexibleDateTime;

/**
 * Represents a task with a specific deadline.
 * This task type includes a "by" date indicating when the task should be completed.
 */
public class Deadline extends Task {
    
    protected FlexibleDateTime by;
    
    /**
     * Constructs a new Deadline task with the specified description and due date.
     * 
     * @param description the description of the deadline task
     * @param by the due date/time for the task
     * @throws ZenException if the description or by date is null or empty
     */
    public Deadline(String description, String by) throws ZenException {
        super(description, TaskType.DEADLINE);
        if (by == null || by.trim().isEmpty()) {
            throw new ZenException("The deadline date cannot be empty.");
        }
        this.by = new FlexibleDateTime(by.trim());
    }

    public FlexibleDateTime getFlexibleBy() {
        return by;
    }

    public String getBy() {
        return by.toString();
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by.toString() + ")";
    }
} 