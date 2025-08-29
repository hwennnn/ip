public class Deadline extends Task {
    
    protected FlexibleDateTime by;
    
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