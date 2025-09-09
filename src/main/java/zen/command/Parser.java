package zen.command;

import zen.exception.ZenException;
import zen.task.Deadline;
import zen.task.Event;

/**
 * Handles parsing of user commands and extracting relevant information
 */
public class Parser {
    // Command prefixes
    private static final String TODO_PREFIX = "todo ";
    private static final String MARK_PREFIX = "mark ";
    private static final String UNMARK_PREFIX = "unmark ";
    private static final String DELETE_PREFIX = "delete ";
    private static final String DEADLINE_PREFIX = "deadline ";
    private static final String EVENT_PREFIX = "event ";
    private static final String FIND_PREFIX = "find ";

    // Command format constants
    private static final int TODO_PREFIX_LENGTH = 5;
    private static final int FIND_PREFIX_LENGTH = 5;
    private static final int DEADLINE_PREFIX_LENGTH = 9;
    private static final int EVENT_PREFIX_LENGTH = 6;

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

        // Handle single-word commands first
        CommandType singleWordCommand = parseSingleWordCommand(command);
        if (singleWordCommand != null) {
            return singleWordCommand;
        }

        // Handle commands with parameters
        CommandType parameterCommand = parseParameterCommand(command);
        if (parameterCommand != null) {
            return parameterCommand;
        }

        return CommandType.UNKNOWN;
    }

    /**
     * Parses single-word commands (no parameters)
     *
     * @param command the trimmed command string
     * @return CommandType if it's a single-word command, null otherwise
     */
    private static CommandType parseSingleWordCommand(String command) {
        switch (command) {
        case "bye":
            return CommandType.BYE;
        case "list":
            return CommandType.LIST;
        case "todo":
            return CommandType.TODO_EMPTY;
        case "deadline":
            return CommandType.DEADLINE_EMPTY;
        case "event":
            return CommandType.EVENT_EMPTY;
        case "find":
            return CommandType.FIND_EMPTY;
        default:
            return null;
        }
    }

    /**
     * Parses commands that have parameters
     *
     * @param command the trimmed command string
     * @return CommandType if it's a valid parameter command, null otherwise
     */
    private static CommandType parseParameterCommand(String command) {
        if (command.startsWith(MARK_PREFIX)) {
            return CommandType.MARK;
        }
        if (command.startsWith(UNMARK_PREFIX)) {
            return CommandType.UNMARK;
        }
        if (command.startsWith(DELETE_PREFIX)) {
            return CommandType.DELETE;
        }
        if (command.startsWith(TODO_PREFIX)) {
            return CommandType.TODO;
        }
        if (command.startsWith(DEADLINE_PREFIX)) {
            return CommandType.DEADLINE;
        }
        if (command.startsWith(EVENT_PREFIX)) {
            return CommandType.EVENT;
        }
        if (command.startsWith(FIND_PREFIX)) {
            return CommandType.FIND;
        }
        return null;
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
        return command.substring(TODO_PREFIX_LENGTH).trim();
    }

    /**
     * Extracts the keyword from find commands
     *
     * @param command the find command
     * @return the search keyword
     */
    public static String parseFindKeyword(String command) {
        return command.substring(FIND_PREFIX_LENGTH).trim();
    }

    /**
     * Parses deadline command and extracts description and by date
     *
     * @param command the deadline command
     * @return Deadline object if valid format, null otherwise
     * @throws ZenException if there's an error creating the deadline task
     */
    public static Deadline parseDeadline(String command) throws ZenException {
        String remaining = command.substring(DEADLINE_PREFIX_LENGTH).trim();
        int byIndex = remaining.indexOf(" /by ");

        if (isValidDeadlineFormat(remaining, byIndex)) {
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
     * @return Event object if valid format, null otherwise
     * @throws ZenException if there's an error creating the event task
     */
    public static Event parseEvent(String command) throws ZenException {
        String remaining = command.substring(EVENT_PREFIX_LENGTH).trim();
        int fromIndex = remaining.indexOf(" /from ");
        int toIndex = remaining.indexOf(" /to ");

        if (isValidEventFormat(remaining, fromIndex, toIndex)) {
            String description = remaining.substring(0, fromIndex).trim();
            String from = remaining.substring(fromIndex + 7, toIndex).trim();
            String to = remaining.substring(toIndex + 5).trim();
            return new Event(description, from, to);
        }

        return null;
    }

    /**
     * Validates the format of a deadline command
     *
     * @param remaining the command string after removing the prefix
     * @param byIndex   the index of " /by " in the remaining string
     * @return true if the format is valid, false otherwise
     */
    private static boolean isValidDeadlineFormat(String remaining, int byIndex) {
        return byIndex != -1 && byIndex + 5 < remaining.length();
    }

    /**
     * Validates the format of an event command
     *
     * @param remaining the command string after removing the prefix
     * @param fromIndex the index of " /from " in the remaining string
     * @param toIndex   the index of " /to " in the remaining string
     * @return true if the format is valid, false otherwise
     */
    private static boolean isValidEventFormat(String remaining, int fromIndex, int toIndex) {
        return fromIndex != -1 && toIndex != -1 && fromIndex < toIndex && toIndex + 5 < remaining.length();
    }

    /**
     * Enum representing different command types
     */
    public enum CommandType {
        BYE, LIST, MARK, UNMARK, DELETE,
        TODO, TODO_EMPTY, DEADLINE, DEADLINE_EMPTY,
        EVENT, EVENT_EMPTY, FIND, FIND_EMPTY, UNKNOWN, EMPTY
    }
}
