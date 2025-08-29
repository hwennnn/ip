package zen.task;

import zen.exception.ZenException;
import zen.util.FlexibleDateTime;

/**
 * Represents an event task that occurs within a specific time period.
 * This task type includes both a start time ("from") and end time ("to").
 */
public class Event extends Task {

    protected FlexibleDateTime from;
    protected FlexibleDateTime to;

    /**
     * Constructs a new Event task with the specified description, start time, and end time.
     *
     * @param description the description of the event task
     * @param from        the start date/time of the event
     * @param to          the end date/time of the event
     * @throws ZenException if the description, from time, or to time is null or empty
     */
    public Event(String description, String from, String to) throws ZenException {
        super(description, TaskType.EVENT);
        if (from == null || from.trim().isEmpty()) {
            throw new ZenException("The event start time cannot be empty.");
        }
        if (to == null || to.trim().isEmpty()) {
            throw new ZenException("The event end time cannot be empty.");
        }
        this.from = new FlexibleDateTime(from.trim());
        this.to = new FlexibleDateTime(to.trim());
    }

    public String getFrom() {
        return from.toString();
    }

    public String getTo() {
        return to.toString();
    }

    public FlexibleDateTime getFlexibleFrom() {
        return from;
    }

    public FlexibleDateTime getFlexibleTo() {
        return to;
    }


    @Override
    public String toString() {
        return super.toString() + " (from: " + from.toString() + " to: " + to.toString() + ")";
    }
} 