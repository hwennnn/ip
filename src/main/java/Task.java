abstract class Task {
    protected String description;
    protected boolean isDone;
    protected TaskType taskType;

    public Task(String description, TaskType taskType) throws ZenException {
        if (description == null || description.trim().isEmpty()) {
            throw new ZenException("The description of a task cannot be empty.");
        }
        this.description = description.trim();
        this.isDone = false;
        this.taskType = taskType;
    }

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

    @Override
    public String toString() {
        return taskType + "[" + getStatusIcon() + "] " + description;
    }
} 