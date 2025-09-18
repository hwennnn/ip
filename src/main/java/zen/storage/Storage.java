package zen.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import zen.exception.ZenException;
import zen.task.Deadline;
import zen.task.Event;
import zen.task.Task;
import zen.task.Todo;

/**
 * Handles the loading and saving of tasks to and from a file.
 * This class manages task persistence using a simple text-based format.
 */
public class Storage {
    // File and directory constants
    private static final String DATA_DIRECTORY = "data";
    private static final String DATA_FILE = "zen.txt";

    // File format constants
    private static final String FIELD_SEPARATOR = " | ";
    private static final String DONE_STATUS = "1";
    private static final String NOT_DONE_STATUS = "0";
    private static final String TASK_TYPE_TODO = "T";
    private static final String TASK_TYPE_DEADLINE = "D";
    private static final String TASK_TYPE_EVENT = "E";

    // Minimum field requirements
    private static final int MIN_TASK_FIELDS = 3;
    private static final int MIN_DEADLINE_FIELDS = 4;
    private static final int MIN_EVENT_FIELDS = 5;

    private final Path dataPath;

    /**
     * Constructs a Storage instance with the given data file location
     */
    public Storage(String dataFile) {
        this.dataPath = Paths.get(DATA_DIRECTORY, dataFile);
    }

    /**
     * Constructs a Storage instance with the default data file location
     */
    public Storage() {
        this(DATA_FILE);
    }

    /**
     * Loads tasks from the data file.
     * Creates the data directory and file if they don't exist.
     *
     * @return ArrayList of tasks loaded from file
     * @throws ZenException if there's an error loading tasks
     */
    public ArrayList<Task> load() throws ZenException {
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            // Create data directory if it doesn't exist
            Files.createDirectories(dataPath.getParent());

            // If file doesn't exist, return empty list
            if (!Files.exists(dataPath)) {
                return tasks;
            }

            // Read and parse each line
            List<String> lines = Files.readAllLines(dataPath);
            for (String line : lines) {
                try {
                    Task task = parseTaskFromLine(line);
                    if (task != null) {
                        tasks.add(task);
                    }
                } catch (Exception e) {
                    System.out.println("Warning: Skipping corrupted line: " + line);
                    System.out.println("Error message: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            throw new ZenException("Failed to load tasks: " + e.getMessage());
        }

        return tasks;
    }

    /**
     * Saves tasks to the data file.
     *
     * @param tasks ArrayList of tasks to save
     * @throws ZenException if there's an error saving tasks
     */
    public void save(ArrayList<Task> tasks) throws ZenException {
        try {
            // Create data directory if it doesn't exist
            Files.createDirectories(dataPath.getParent());

            // Convert tasks to file format and write to file
            List<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                lines.add(taskToFileFormat(task));
            }

            Files.write(dataPath, lines);

        } catch (IOException e) {
            throw new ZenException("Failed to save tasks: " + e.getMessage());
        }
    }

    /**
     * Converts a task to the file format string.
     * Format: TYPE | STATUS | DESCRIPTION [| ADDITIONAL_INFO]
     *
     * @param task Task to convert
     * @return String representation for file
     */
    private String taskToFileFormat(Task task) {
        String status = task.isDone() ? DONE_STATUS : NOT_DONE_STATUS;
        String type = task.getTaskType().getSymbol();

        if (task instanceof Todo) {
            return formatTodoTask(type, status, task.getDescription());
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return formatDeadlineTask(type, status, task.getDescription(), deadline.getBy());
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return formatEventTask(type, status, task.getDescription(), event.getFrom(), event.getTo());
        }

        return formatTodoTask(type, status, task.getDescription());
    }

    /**
     * Formats a todo task for file storage
     */
    private String formatTodoTask(String type, String status, String description) {
        return type + FIELD_SEPARATOR + status + FIELD_SEPARATOR + description;
    }

    /**
     * Formats a deadline task for file storage
     */
    private String formatDeadlineTask(String type, String status, String description, String by) {
        return type + FIELD_SEPARATOR + status + FIELD_SEPARATOR + description + FIELD_SEPARATOR + by;
    }

    /**
     * Formats an event task for file storage
     */
    private String formatEventTask(String type, String status, String description, String from, String to) {
        return type + FIELD_SEPARATOR + status + FIELD_SEPARATOR + description 
                + FIELD_SEPARATOR + from + FIELD_SEPARATOR + to;
    }

    /**
     * Parses a line from the file to create a Task object.
     *
     * @param line Line from the file
     * @return Task object or null if parsing fails
     * @throws ZenException if task creation fails
     */
    private Task parseTaskFromLine(String line) throws ZenException {
        if (isEmptyLine(line)) {
            return null;
        }

        String[] parts = line.split(" \\| ");
        validateMinimumFields(parts);

        String type = parts[0].trim();
        boolean isDone = DONE_STATUS.equals(parts[1].trim());
        String description = parts[2].trim();

        Task task = createTaskByType(type, description, parts);

        if (task != null && isDone) {
            task.markAsDone();
        }

        return task;
    }

    /**
     * Checks if a line is empty or null
     */
    private boolean isEmptyLine(String line) {
        return line == null || line.trim().isEmpty();
    }

    /**
     * Validates that the line has the minimum required fields
     */
    private void validateMinimumFields(String[] parts) {
        if (parts.length < MIN_TASK_FIELDS) {
            throw new IllegalArgumentException("Invalid line format");
        }
    }

    /**
     * Creates a task based on its type
     */
    private Task createTaskByType(String type, String description, String[] parts) throws ZenException {
        switch (type) {
        case TASK_TYPE_TODO:
            return new Todo(description);
        case TASK_TYPE_DEADLINE:
            return createDeadlineTask(description, parts);
        case TASK_TYPE_EVENT:
            return createEventTask(description, parts);
        default:
            throw new IllegalArgumentException("Unknown task type: " + type);
        }
    }

    /**
     * Creates a deadline task from parsed parts
     */
    private Task createDeadlineTask(String description, String[] parts) throws ZenException {
        if (parts.length < MIN_DEADLINE_FIELDS) {
            throw new IllegalArgumentException("Deadline missing date");
        }
        String by = parts[3].trim();
        return new Deadline(description, by);
    }

    /**
     * Creates an event task from parsed parts
     */
    private Task createEventTask(String description, String[] parts) throws ZenException {
        if (parts.length < MIN_EVENT_FIELDS) {
            throw new IllegalArgumentException("Event missing time information");
        }
        String from = parts[3].trim();
        String to = parts[4].trim();
        return new Event(description, from, to);
    }
}