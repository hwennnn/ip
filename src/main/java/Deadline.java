public class Deadline extends Task {
    
    protected String by;
    
    public Deadline(String description, String by) throws ZenException {
        super(description, TaskType.DEADLINE);
        if (by == null || by.trim().isEmpty()) {
            throw new ZenException("The deadline date cannot be empty.");
        }
        this.by = by.trim();
    }
    
    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }
} 