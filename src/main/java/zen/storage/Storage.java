package zen.storage;

import zen.exception.ZenException;
import zen.task.Deadline;
import zen.task.Event;
import zen.task.Task;
import zen.task.Todo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private static final String DATA_DIRECTORY = "data";
    private static final String DATA_FILE = "duke.txt";
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
        String status = task.isDone() ? "1" : "0";
        String type = task.getTaskType().getSymbol();

        if (task instanceof Todo) {
            return type + " | " + status + " | " + task.getDescription();
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return type + " | " + status + " | " + task.getDescription() + " | " + deadline.getBy();
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return type + " | " + status + " | " + task.getDescription() + " | " + event.getFrom() + " | " + event.getTo();
        }

        return type + " | " + status + " | " + task.getDescription();
    }

    /**
     * Parses a line from the file to create a Task object.
     *
     * @param line Line from the file
     * @return Task object or null if parsing fails
     * @throws ZenException if task creation fails
     */
    private Task parseTaskFromLine(String line) throws ZenException {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid line format");
        }

        String type = parts[0].trim();
        boolean isDone = "1".equals(parts[1].trim());
        String description = parts[2].trim();

        Task task = null;

        switch (type) {
        case "T":
            task = new Todo(description);
            break;
        case "D":
            // D | 0 | return book | June 6th
            if (parts.length < 4) {
                throw new IllegalArgumentException("Deadline missing date");
            }
            String by = parts[3].trim();
            task = new Deadline(description, by);
            break;
        case "E":
            // E | 0 | project meeting | Aug 6th 2pm | Aug 7th 9pm
            if (parts.length < 5) {
                throw new IllegalArgumentException("Event missing time information");
            }
            String from = parts[3].trim();
            String to = parts[4].trim();
            task = new Event(description, from, to);
            break;
        default:
            throw new IllegalArgumentException("Unknown task type: " + type);
        }

        if (task != null && isDone) {
            task.markAsDone();
        }

        return task;
    }
}