package zen;

import zen.command.Parser;
import zen.exception.ZenException;
import zen.storage.Storage;
import zen.task.Deadline;
import zen.task.Event;
import zen.task.Task;
import zen.task.TaskList;
import zen.task.Todo;
import zen.ui.Ui;

/**
 * Main class for the Zen task management chatbot
 */
public class Zen {
    private final Storage storage;
    private final Ui ui;
    private TaskList tasks;

    /**
     * Constructs a Zen instance with default data file location
     */
    public Zen() {
        ui = new Ui();
        storage = new Storage();
        try {
            tasks = new TaskList(storage.load());
        } catch (ZenException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * Main method to start the application
     */
    public static void main(String[] args) {
        new Zen().run();
    }

    /**
     * Runs the main application loop
     */
    public void run() {
        ui.showWelcome();

        while (true) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();

                if (handleCommand(fullCommand)) {
                    break; // Exit if bye command
                }

            } catch (ZenException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }

        ui.close();
    }

    /**
     * Handles a single command and returns whether to exit
     *
     * @param fullCommand the command to handle
     * @return true if the application should exit, false otherwise
     * @throws ZenException if there's an error processing the command
     */
    private boolean handleCommand(String fullCommand) throws ZenException {
        Parser.CommandType commandType = Parser.parseCommand(fullCommand);

        switch (commandType) {
        case BYE:
            ui.showGoodbye();
            return true;

        case LIST:
            ui.showTaskList(tasks.getTasks());
            break;

        case MARK:
            handleMarkCommand(fullCommand);
            break;

        case UNMARK:
            handleUnmarkCommand(fullCommand);
            break;

        case DELETE:
            handleDeleteCommand(fullCommand);
            break;

        case TODO_EMPTY:
            ui.showError("The description of a todo cannot be empty.");
            break;

        case TODO:
            handleTodoCommand(fullCommand);
            break;

        case DEADLINE_EMPTY:
            ui.showError("Please use the format: deadline <description> /by <date>");
            break;

        case DEADLINE:
            handleDeadlineCommand(fullCommand);
            break;

        case EVENT_EMPTY:
            ui.showError("Please use the format: event <description> /from <start> /to <end>");
            break;

        case EVENT:
            handleEventCommand(fullCommand);
            break;

        case EMPTY:
            ui.showError("Please enter a command.");
            break;

        case UNKNOWN:
        default:
            ui.showError("I'm sorry, but I don't know what that means :-(");
            break;
        }

        return false;
    }

    /**
     * Handles mark command
     */
    private void handleMarkCommand(String command) throws ZenException {
        int index = Parser.parseTaskIndex(command, "mark ");
        if (index == -1) {
            ui.showError("Please provide a valid task number!");
            return;
        }

        try {
            Task task = tasks.markTask(index);
            storage.save(tasks.getTasks());
            ui.showTaskMarked(task);
        } catch (IndexOutOfBoundsException e) {
            ui.showError(e.getMessage());
        }
    }

    /**
     * Handles unmark command
     */
    private void handleUnmarkCommand(String command) throws ZenException {
        int index = Parser.parseTaskIndex(command, "unmark ");
        if (index == -1) {
            ui.showError("Please provide a valid task number!");
            return;
        }

        try {
            Task task = tasks.unmarkTask(index);
            storage.save(tasks.getTasks());
            ui.showTaskUnmarked(task);
        } catch (IndexOutOfBoundsException e) {
            ui.showError(e.getMessage());
        }
    }

    /**
     * Handles delete command
     */
    private void handleDeleteCommand(String command) throws ZenException {
        int index = Parser.parseTaskIndex(command, "delete ");
        if (index == -1) {
            ui.showError("Please provide a valid task number!");
            return;
        }

        try {
            Task task = tasks.deleteTask(index);
            storage.save(tasks.getTasks());
            ui.showTaskDeleted(task, tasks.size());
        } catch (IndexOutOfBoundsException e) {
            ui.showError(e.getMessage());
        }
    }

    /**
     * Handles todo command
     */
    private void handleTodoCommand(String command) throws ZenException {
        String description = Parser.parseTodoDescription(command);
        Task task = new Todo(description);
        tasks.addTask(task);
        storage.save(tasks.getTasks());
        ui.showTaskAdded(task, tasks.size());
    }

    /**
     * Handles deadline command
     */
    private void handleDeadlineCommand(String command) throws ZenException {
        Deadline info = Parser.parseDeadline(command);
        if (info == null) {
            ui.showError("Please use the format: deadline <description> /by <date>");
            return;
        }

        tasks.addTask(info);
        storage.save(tasks.getTasks());
        ui.showTaskAdded(info, tasks.size());
    }

    /**
     * Handles event command
     */
    private void handleEventCommand(String command) throws ZenException {
        Event info = Parser.parseEvent(command);
        if (info == null) {
            ui.showError("Please use the format: event <description> /from <start> /to <end>");
            return;
        }

        tasks.addTask(info);
        storage.save(tasks.getTasks());
        ui.showTaskAdded(info, tasks.size());
    }
}