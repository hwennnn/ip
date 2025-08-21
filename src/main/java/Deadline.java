public class Deadline extends Task {
    
    protected String by;
    
    public Deadline(String description, String by) throws ZenException {
        super(description);
        if (by == null || by.trim().isEmpty()) {
            throw new ZenException("The deadline date cannot be empty.");
        }
        this.by = by.trim();
    }
    
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
} 