package zen.command;

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
 * Handles the execution of parsed commands.
 * This class follows the Single Responsibility Principle by separating
 * command execution logic from the main application flow.
 */
public class CommandExecutor {
    // AI-Enhanced: More user-friendly and descriptive error messages
    private static final String ERROR_TODO_EMPTY = "📝 Oops! Your todo needs a description. Try: todo buy groceries";
    private static final String ERROR_DEADLINE_FORMAT = "📅 Let me help! Use this format: deadline <task> /by <date>\n"
            + "Example: deadline submit report /by 2024-12-01";
    private static final String ERROR_EVENT_FORMAT = "📆 Almost there! Use this format: event <task> /from <start> /to <end>\n"
            + "Example: event meeting /from 2024-12-01 14:00 /to 2024-12-01 15:00";
    private static final String ERROR_FIND_EMPTY = "🔍 What should I search for? Try: find meeting";
    private static final String ERROR_EMPTY_COMMAND = "💭 I'm listening! Type a command like 'list', 'todo', or 'help'";
    private static final String ERROR_UNKNOWN_COMMAND = "🤔 I don't recognize that command. Type 'help' to see what I can do!";
    private static final String ERROR_INVALID_TASK_NUMBER = "🔢 Please provide a valid task number (check 'list')!";

    private final TaskList tasks;
    private final Storage storage;
    private final Ui ui;
    private Task lastDeletedTask; // Store for GUI response

    /**
     * Constructs a CommandExecutor with the required dependencies.
     *
     * @param tasks   the task list to operate on
     * @param storage the storage instance for persistence
     * @param ui      the UI instance for console output (can be null for GUI mode)
     */
    public CommandExecutor(TaskList tasks, Storage storage, Ui ui) {
        this.tasks = tasks;
        this.storage = storage;
        this.ui = ui;
    }

    /**
     * Executes a command for console mode.
     *
     * @param fullCommand the command string to execute
     * @return true if the application should exit (bye command), false otherwise
     * @throws ZenException if there's an error executing the command
     */
    public boolean executeCommand(String fullCommand) throws ZenException {
        Parser.CommandType commandType = Parser.parseCommand(fullCommand);
        return executeCommandByType(commandType, fullCommand, false);
    }

    /**
     * Executes a command for GUI mode and returns the response.
     *
     * @param fullCommand the command string to execute
     * @return the response string for the GUI
     * @throws ZenException if there's an error executing the command
     */
    public String executeCommandForGui(String fullCommand) throws ZenException {
        Parser.CommandType commandType = Parser.parseCommand(fullCommand);
        executeCommandByType(commandType, fullCommand, true);
        return getGuiResponse(commandType, fullCommand);
    }

    /**
     * Executes a command based on its type.
     *
     * @param commandType the type of command to execute
     * @param fullCommand the full command string
     * @param isGuiMode   whether this is being executed for GUI mode
     * @return true if the application should exit, false otherwise
     * @throws ZenException if there's an error executing the command
     */
    private boolean executeCommandByType(Parser.CommandType commandType, String fullCommand, boolean isGuiMode)
            throws ZenException {
        switch (commandType) {
        case BYE:
            return handleByeCommand(isGuiMode);
        case LIST:
            handleListCommand(isGuiMode);
            break;
        case HELP:
            handleHelpCommand(isGuiMode);
            break;
        case MARK:
            handleMarkCommand(fullCommand, isGuiMode);
            break;
        case UNMARK:
            handleUnmarkCommand(fullCommand, isGuiMode);
            break;
        case DELETE:
            handleDeleteCommand(fullCommand, isGuiMode);
            break;
        case TODO_EMPTY:
            handleTodoEmptyCommand(isGuiMode);
            break;
        case TODO:
            handleTodoCommand(fullCommand, isGuiMode);
            break;
        case DEADLINE_EMPTY:
            handleDeadlineEmptyCommand(isGuiMode);
            break;
        case DEADLINE:
            handleDeadlineCommand(fullCommand, isGuiMode);
            break;
        case EVENT_EMPTY:
            handleEventEmptyCommand(isGuiMode);
            break;
        case EVENT:
            handleEventCommand(fullCommand, isGuiMode);
            break;
        case FIND_EMPTY:
            handleFindEmptyCommand(isGuiMode);
            break;
        case FIND:
            handleFindCommand(fullCommand, isGuiMode);
            break;
        case EMPTY:
            handleEmptyCommand(isGuiMode);
            break;
        case UNKNOWN:
        default:
            handleUnknownCommand(isGuiMode);
            break;
        }
        return false;
    }

    private boolean handleByeCommand(boolean isGuiMode) {
        if (!isGuiMode && ui != null) {
            ui.showGoodbye();
        }
        return true;
    }

    private void handleListCommand(boolean isGuiMode) {
        if (!isGuiMode && ui != null) {
            ui.showTaskList(tasks.getTasks());
        }
    }

    private void handleHelpCommand(boolean isGuiMode) {
        if (!isGuiMode && ui != null) {
            ui.showHelp();
        }
    }

    private void handleMarkCommand(String command, boolean isGuiMode) throws ZenException {
        int index = Parser.parseTaskIndex(command, "mark ");
        if (index == -1) {
            if (isGuiMode) {
                throw new ZenException(ERROR_INVALID_TASK_NUMBER);
            } else if (ui != null) {
                ui.showError(ERROR_INVALID_TASK_NUMBER);
            }
            return;
        }

        try {
            Task task = tasks.markTask(index);
            storage.save(tasks.getTasks());
            if (!isGuiMode && ui != null) {
                ui.showTaskMarked(task);
            }
        } catch (IndexOutOfBoundsException e) {
            if (isGuiMode) {
                throw new ZenException(e.getMessage());
            } else if (ui != null) {
                ui.showError(e.getMessage());
            }
        }
    }

    private void handleUnmarkCommand(String command, boolean isGuiMode) throws ZenException {
        int index = Parser.parseTaskIndex(command, "unmark ");
        if (index == -1) {
            if (isGuiMode) {
                throw new ZenException(ERROR_INVALID_TASK_NUMBER);
            } else if (ui != null) {
                ui.showError(ERROR_INVALID_TASK_NUMBER);
            }
            return;
        }

        try {
            Task task = tasks.unmarkTask(index);
            storage.save(tasks.getTasks());
            if (!isGuiMode && ui != null) {
                ui.showTaskUnmarked(task);
            }
        } catch (IndexOutOfBoundsException e) {
            if (isGuiMode) {
                throw new ZenException(e.getMessage());
            } else if (ui != null) {
                ui.showError(e.getMessage());
            }
        }
    }

    private void handleDeleteCommand(String command, boolean isGuiMode) throws ZenException {
        int index = Parser.parseTaskIndex(command, "delete ");
        if (index == -1) {
            if (isGuiMode) {
                throw new ZenException(ERROR_INVALID_TASK_NUMBER);
            } else if (ui != null) {
                ui.showError(ERROR_INVALID_TASK_NUMBER);
            }
            return;
        }

        try {
            Task task = tasks.deleteTask(index);
            lastDeletedTask = task; // Store for GUI response
            storage.save(tasks.getTasks());
            if (!isGuiMode && ui != null) {
                ui.showTaskDeleted(task, tasks.size());
            }
        } catch (IndexOutOfBoundsException e) {
            if (isGuiMode) {
                throw new ZenException(e.getMessage());
            } else if (ui != null) {
                ui.showError(e.getMessage());
            }
        }
    }

    private void handleTodoEmptyCommand(boolean isGuiMode) throws ZenException {
        if (isGuiMode) {
            throw new ZenException(ERROR_TODO_EMPTY);
        } else if (ui != null) {
            ui.showError(ERROR_TODO_EMPTY);
        }
    }

    private void handleTodoCommand(String command, boolean isGuiMode) throws ZenException {
        String description = Parser.parseTodoDescription(command);
        Task task = new Todo(description);
        tasks.addTask(task);
        storage.save(tasks.getTasks());
        if (!isGuiMode && ui != null) {
            ui.showTaskAdded(task, tasks.size());
        }
    }

    private void handleDeadlineEmptyCommand(boolean isGuiMode) throws ZenException {
        if (isGuiMode) {
            throw new ZenException(ERROR_DEADLINE_FORMAT);
        } else if (ui != null) {
            ui.showError(ERROR_DEADLINE_FORMAT);
        }
    }

    private void handleDeadlineCommand(String command, boolean isGuiMode) throws ZenException {
        Deadline deadline = Parser.parseDeadline(command);
        if (deadline == null) {
            if (isGuiMode) {
                throw new ZenException(ERROR_DEADLINE_FORMAT);
            } else if (ui != null) {
                ui.showError(ERROR_DEADLINE_FORMAT);
            }
            return;
        }

        tasks.addTask(deadline);
        storage.save(tasks.getTasks());
        if (!isGuiMode && ui != null) {
            ui.showTaskAdded(deadline, tasks.size());
        }
    }

    private void handleEventEmptyCommand(boolean isGuiMode) throws ZenException {
        if (isGuiMode) {
            throw new ZenException(ERROR_EVENT_FORMAT);
        } else if (ui != null) {
            ui.showError(ERROR_EVENT_FORMAT);
        }
    }

    private void handleEventCommand(String command, boolean isGuiMode) throws ZenException {
        Event event = Parser.parseEvent(command);
        if (event == null) {
            if (isGuiMode) {
                throw new ZenException(ERROR_EVENT_FORMAT);
            } else if (ui != null) {
                ui.showError(ERROR_EVENT_FORMAT);
            }
            return;
        }

        tasks.addTask(event);
        storage.save(tasks.getTasks());
        if (!isGuiMode && ui != null) {
            ui.showTaskAdded(event, tasks.size());
        }
    }

    private void handleFindEmptyCommand(boolean isGuiMode) throws ZenException {
        if (isGuiMode) {
            throw new ZenException(ERROR_FIND_EMPTY);
        } else if (ui != null) {
            ui.showError(ERROR_FIND_EMPTY);
        }
    }

    private void handleFindCommand(String command, boolean isGuiMode) {
        String keyword = Parser.parseFindKeyword(command);
        if (!isGuiMode && ui != null) {
            ui.showMatchingTasks(tasks.findTasksContaining(keyword));
        }
    }

    private void handleEmptyCommand(boolean isGuiMode) throws ZenException {
        if (isGuiMode) {
            throw new ZenException(ERROR_EMPTY_COMMAND);
        } else if (ui != null) {
            ui.showError(ERROR_EMPTY_COMMAND);
        }
    }

    private void handleUnknownCommand(boolean isGuiMode) throws ZenException {
        if (isGuiMode) {
            throw new ZenException(ERROR_UNKNOWN_COMMAND);
        } else if (ui != null) {
            ui.showError(ERROR_UNKNOWN_COMMAND);
        }
    }

    /**
     * Gets the GUI response for a command that has already been executed.
     *
     * @param commandType the type of command that was executed
     * @param fullCommand the full command string
     * @return the response string for the GUI
     */
    private String getGuiResponse(Parser.CommandType commandType, String fullCommand) throws ZenException {
        switch (commandType) {
        case BYE:
            return GuiResponseFormatter.formatGoodbye();
        case LIST:
            return GuiResponseFormatter.formatTaskList(tasks.getTasks());
        case HELP:
            return GuiResponseFormatter.formatHelp();
        case MARK:
            return getMarkResponse(fullCommand);
        case UNMARK:
            return getUnmarkResponse(fullCommand);
        case DELETE:
            return getDeleteResponse(fullCommand);
        case TODO:
            return getLastTaskAddedResponse();
        case DEADLINE:
            return getLastTaskAddedResponse();
        case EVENT:
            return getLastTaskAddedResponse();
        case FIND:
            String keyword = Parser.parseFindKeyword(fullCommand);
            return GuiResponseFormatter.formatMatchingTasks(tasks.findTasksContaining(keyword));
        default:
            // Error cases are thrown as exceptions in executeCommandByType
            return "";
        }
    }

    private String getMarkResponse(String command) throws ZenException {
        int index = Parser.parseTaskIndex(command, "mark ");
        if (index == -1 || index >= tasks.size()) {
            throw new ZenException(ERROR_INVALID_TASK_NUMBER);
        }
        return GuiResponseFormatter.formatTaskMarked(tasks.getTask(index));
    }

    private String getUnmarkResponse(String command) throws ZenException {
        int index = Parser.parseTaskIndex(command, "unmark ");
        if (index == -1 || index >= tasks.size()) {
            throw new ZenException(ERROR_INVALID_TASK_NUMBER);
        }
        return GuiResponseFormatter.formatTaskUnmarked(tasks.getTask(index));
    }

    private String getDeleteResponse(String command) throws ZenException {
        if (lastDeletedTask == null) {
            throw new ZenException("No task was deleted.");
        }
        return GuiResponseFormatter.formatTaskDeleted(lastDeletedTask, tasks.size());
    }

    private String getLastTaskAddedResponse() {
        if (tasks.size() > 0) {
            Task lastTask = tasks.getTask(tasks.size() - 1);
            return GuiResponseFormatter.formatTaskAdded(lastTask, tasks.size());
        }
        return "Task added successfully.";
    }
}
