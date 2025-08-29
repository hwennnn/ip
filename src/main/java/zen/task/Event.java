package zen.task;

import zen.exception.ZenException;
import zen.util.FlexibleDateTime;

public class Event extends Task {
    
    protected FlexibleDateTime from;
    protected FlexibleDateTime to;
    
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