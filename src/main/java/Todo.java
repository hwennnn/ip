public class Todo extends Task {
    
    public Todo(String description) throws ZenException {
        super(description, TaskType.TODO);
    }
    
    @Override
    public String toString() {
        return super.toString();
    }
} 