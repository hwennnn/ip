public class Event extends Task {
    
    protected String from;
    protected String to;
    
    public Event(String description, String from, String to) throws ZenException {
        super(description, TaskType.EVENT);
        if (from == null || from.trim().isEmpty()) {
            throw new ZenException("The event start time cannot be empty.");
        }
        if (to == null || to.trim().isEmpty()) {
            throw new ZenException("The event end time cannot be empty.");
        }
        this.from = from.trim();
        this.to = to.trim();
    }
    
    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }
} 