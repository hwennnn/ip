package zen.command;

import zen.exception.ZenException;
import zen.task.Deadline;
import zen.task.Event;

/**
 * Handles parsing of user commands and extracting relevant information
 */
public class Parser {

    /**
     * Parses a user command and returns the command type
     *
     * @param fullCommand the full command string from user
     * @return CommandType representing the parsed command
     */
    public static CommandType parseCommand(String fullCommand) {
        if (fullCommand == null || fullCommand.trim().isEmpty()) {
            return CommandType.EMPTY;
        }

        String command = fullCommand.trim();

        if (command.equals("bye")) {
            return CommandType.BYE;
        } else if (command.equals("list")) {
            return CommandType.LIST;
        } else if (command.startsWith("mark ")) {
            return CommandType.MARK;
        } else if (command.startsWith("unmark ")) {
            return CommandType.UNMARK;
        } else if (command.startsWith("delete ")) {
            return CommandType.DELETE;
        } else if (command.equals("todo")) {
            return CommandType.TODO_EMPTY;
        } else if (command.startsWith("todo ")) {
            return CommandType.TODO;
        } else if (command.equals("deadline")) {
            return CommandType.DEADLINE_EMPTY;
        } else if (command.startsWith("deadline ")) {
            return CommandType.DEADLINE;
        } else if (command.equals("event")) {
            return CommandType.EVENT_EMPTY;
        } else if (command.startsWith("event ")) {
            return CommandType.EVENT;
        } else {
            return CommandType.UNKNOWN;
        }
    }

    /**
     * Extracts the task index from mark/unmark/delete commands
     *
     * @param command the command string
     * @param prefix  the command prefix (e.g., "mark ", "unmark ", "delete ")
     * @return the task index (0-based) or -1 if invalid
     */
    public static int parseTaskIndex(String command, String prefix) {
        try {
            String indexStr = command.substring(prefix.length()).trim();
            int index = Integer.parseInt(indexStr) - 1; // Convert to 0-based
            return index >= 0 ? index : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Extracts the description from todo commands
     *
     * @param command the todo command
     * @return the task description
     */
    public static String parseTodoDescription(String command) {
        return command.substring(5).trim(); // Remove "todo "
    }

    /**
     * Parses deadline command and extracts description and by date
     *
     * @param command the deadline command
     * @return DeadlineInfo containing description and by date, or null if invalid format
     */
    public static Deadline parseDeadline(String command) throws ZenException {
        String remaining = command.substring(9).trim(); // Remove "deadline "
        int byIndex = remaining.indexOf(" /by ");

        if (byIndex != -1 && byIndex + 5 < remaining.length()) {
            String description = remaining.substring(0, byIndex).trim();
            String by = remaining.substring(byIndex + 5).trim();
            return new Deadline(description, by);
        }

        return null;
    }

    /**
     * Parses event command and extracts description, from time, and to time
     *
     * @param command the event command
     * @return EventInfo containing description, from, and to times, or null if invalid format
     */
    public static Event parseEvent(String command) throws ZenException {
        String remaining = command.substring(6).trim(); // Remove "event "
        int fromIndex = remaining.indexOf(" /from ");
        int toIndex = remaining.indexOf(" /to ");

        if (fromIndex != -1 && toIndex != -1 && fromIndex < toIndex && toIndex + 5 < remaining.length()) {
            String description = remaining.substring(0, fromIndex).trim();
            String from = remaining.substring(fromIndex + 7, toIndex).trim();
            String to = remaining.substring(toIndex + 5).trim();
            return new Event(description, from, to);
        }

        return null;
    }

    /**
     * Enum representing different command types
     */
    public enum CommandType {
        BYE, LIST, MARK, UNMARK, DELETE,
        TODO, TODO_EMPTY, DEADLINE, DEADLINE_EMPTY,
        EVENT, EVENT_EMPTY, UNKNOWN, EMPTY
    }
}
