package zen;

import java.util.ArrayList;

import zen.command.Parser;
import zen.exception.ZenException;
import zen.storage.Storage;
import zen.task.Deadline;
import zen.task.Event;
import zen.task.Task;
import zen.task.TaskList;
import zen.task.Todo;
import zen.ui.GuiResponseFormatter;
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

        case FIND_EMPTY:
            ui.showError("Please provide a keyword to search for.");
            break;

        case FIND:
            handleFindCommand(fullCommand);
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
        handleMarkCommand(command, false);
    }

    /**
     * Handles mark command with optional return value for GUI
     */
    private String handleMarkCommand(String command, boolean returnResponse) throws ZenException {
        int index = Parser.parseTaskIndex(command, "mark ");
        if (index == -1) {
            if (returnResponse) {
                throw new ZenException("Please provide a valid task number!");
            } else {
                ui.showError("Please provide a valid task number!");
                return null;
            }
        }

        try {
            Task task = tasks.markTask(index);
            storage.save(tasks.getTasks());
            if (returnResponse) {
                return GuiResponseFormatter.formatTaskMarked(task);
            } else {
                ui.showTaskMarked(task);
                return null;
            }
        } catch (IndexOutOfBoundsException e) {
            if (returnResponse) {
                throw new ZenException(e.getMessage());
            } else {
                ui.showError(e.getMessage());
                return null;
            }
        }
    }

    /**
     * Handles unmark command
     */
    private void handleUnmarkCommand(String command) throws ZenException {
        handleUnmarkCommand(command, false);
    }

    /**
     * Handles unmark command with optional return value for GUI
     */
    private String handleUnmarkCommand(String command, boolean returnResponse) throws ZenException {
        int index = Parser.parseTaskIndex(command, "unmark ");
        if (index == -1) {
            if (returnResponse) {
                throw new ZenException("Please provide a valid task number!");
            } else {
                ui.showError("Please provide a valid task number!");
                return null;
            }
        }

        try {
            Task task = tasks.unmarkTask(index);
            storage.save(tasks.getTasks());
            if (returnResponse) {
                return GuiResponseFormatter.formatTaskUnmarked(task);
            } else {
                ui.showTaskUnmarked(task);
                return null;
            }
        } catch (IndexOutOfBoundsException e) {
            if (returnResponse) {
                throw new ZenException(e.getMessage());
            } else {
                ui.showError(e.getMessage());
                return null;
            }
        }
    }

    /**
     * Handles delete command
     */
    private void handleDeleteCommand(String command) throws ZenException {
        handleDeleteCommand(command, false);
    }

    /**
     * Handles delete command with optional return value for GUI
     */
    private String handleDeleteCommand(String command, boolean returnResponse) throws ZenException {
        int index = Parser.parseTaskIndex(command, "delete ");
        if (index == -1) {
            if (returnResponse) {
                throw new ZenException("Please provide a valid task number!");
            } else {
                ui.showError("Please provide a valid task number!");
                return null;
            }
        }

        try {
            Task task = tasks.deleteTask(index);
            storage.save(tasks.getTasks());
            if (returnResponse) {
                return GuiResponseFormatter.formatTaskDeleted(task, tasks.size());
            } else {
                ui.showTaskDeleted(task, tasks.size());
                return null;
            }
        } catch (IndexOutOfBoundsException e) {
            if (returnResponse) {
                throw new ZenException(e.getMessage());
            } else {
                ui.showError(e.getMessage());
                return null;
            }
        }
    }

    /**
     * Handles todo command
     */
    private void handleTodoCommand(String command) throws ZenException {
        handleTodoCommand(command, false);
    }

    /**
     * Handles todo command with optional return value for GUI
     */
    private String handleTodoCommand(String command, boolean returnResponse) throws ZenException {
        String description = Parser.parseTodoDescription(command);
        Task task = new Todo(description);
        tasks.addTask(task);
        storage.save(tasks.getTasks());
        if (returnResponse) {
            return GuiResponseFormatter.formatTaskAdded(task, tasks.size());
        } else {
            ui.showTaskAdded(task, tasks.size());
            return null;
        }
    }

    /**
     * Handles deadline command
     */
    private void handleDeadlineCommand(String command) throws ZenException {
        handleDeadlineCommand(command, false);
    }

    /**
     * Handles deadline command with optional return value for GUI
     */
    private String handleDeadlineCommand(String command, boolean returnResponse) throws ZenException {
        Deadline info = Parser.parseDeadline(command);
        if (info == null) {
            if (returnResponse) {
                throw new ZenException("Please use the format: deadline <description> /by <date>");
            } else {
                ui.showError("Please use the format: deadline <description> /by <date>");
                return null;
            }
        }

        tasks.addTask(info);
        storage.save(tasks.getTasks());
        if (returnResponse) {
            return GuiResponseFormatter.formatTaskAdded(info, tasks.size());
        } else {
            ui.showTaskAdded(info, tasks.size());
            return null;
        }
    }

    /**
     * Handles event command
     */
    private void handleEventCommand(String command) throws ZenException {
        handleEventCommand(command, false);
    }

    /**
     * Handles event command with optional return value for GUI
     */
    private String handleEventCommand(String command, boolean returnResponse) throws ZenException {
        Event info = Parser.parseEvent(command);
        if (info == null) {
            if (returnResponse) {
                throw new ZenException("Please use the format: event <description> /from <start> /to <end>");
            } else {
                ui.showError("Please use the format: event <description> /from <start> /to <end>");
                return null;
            }
        }

        tasks.addTask(info);
        storage.save(tasks.getTasks());
        if (returnResponse) {
            return GuiResponseFormatter.formatTaskAdded(info, tasks.size());
        } else {
            ui.showTaskAdded(info, tasks.size());
            return null;
        }
    }

    /**
     * Handles find command
     */
    private void handleFindCommand(String command) {
        handleFindCommand(command, false);
    }

    /**
     * Handles find command with optional return value for GUI
     */
    private String handleFindCommand(String command, boolean returnResponse) {
        String keyword = Parser.parseFindKeyword(command);
        ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
        if (returnResponse) {
            return GuiResponseFormatter.formatMatchingTasks(matchingTasks);
        } else {
            ui.showMatchingTasks(matchingTasks);
            return null;
        }
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        try {
            return processCommandForResponse(input);
        } catch (ZenException e) {
            return GuiResponseFormatter.formatError(e.getMessage());
        }
    }

    /**
     * Processes a command and returns the appropriate response string
     * @param fullCommand the command to process
     * @return the response string for the GUI
     * @throws ZenException if there's an error processing the command
     */
    private String processCommandForResponse(String fullCommand) throws ZenException {
        Parser.CommandType commandType = Parser.parseCommand(fullCommand);

        switch (commandType) {
        case BYE:
            return GuiResponseFormatter.formatGoodbye();

        case LIST:
            return GuiResponseFormatter.formatTaskList(tasks.getTasks());

        case MARK:
            return handleMarkCommand(fullCommand, true);

        case UNMARK:
            return handleUnmarkCommand(fullCommand, true);

        case DELETE:
            return handleDeleteCommand(fullCommand, true);

        case TODO_EMPTY:
            throw new ZenException("The description of a todo cannot be empty.");

        case TODO:
            return handleTodoCommand(fullCommand, true);

        case DEADLINE_EMPTY:
            throw new ZenException("Please use the format: deadline <description> /by <date>");

        case DEADLINE:
            return handleDeadlineCommand(fullCommand, true);

        case EVENT_EMPTY:
            throw new ZenException("Please use the format: event <description> /from <start> /to <end>");

        case EVENT:
            return handleEventCommand(fullCommand, true);

        case FIND_EMPTY:
            throw new ZenException("Please provide a keyword to search for.");

        case FIND:
            return handleFindCommand(fullCommand, true);

        case EMPTY:
            throw new ZenException("Please enter a command.");

        case UNKNOWN:
        default:
            throw new ZenException("I'm sorry, but I don't know what that means :-(");
        }
    }
}
