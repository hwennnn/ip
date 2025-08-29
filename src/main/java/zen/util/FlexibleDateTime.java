package zen.util;

import zen.exception.ZenException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * A flexible datetime container that can store and handle different variants of date/time:
 * - LocalDateTime (full date and time)
 * - LocalDate (date only)
 * - String (unparseable or custom format defined by user)
 * <p>
 * This class automatically attempts to parse input strings into the most appropriate type
 * and provides consistent formatting and access methods.
 */
public class FlexibleDateTime {

    // Formatters
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM d yyyy");
    // Storage variants
    private LocalDateTime dateTime;
    private LocalDate date;
    private String stringValue;
    // Type tracking
    private DateTimeType type;

    /**
     * Constructor that attempts to parse the input string into the most appropriate type
     *
     * @param input The datetime string to parse
     * @throws ZenException if input is null or empty
     */
    public FlexibleDateTime(String input) throws ZenException {
        // Handle null or empty input
        if (input == null || input.trim().isEmpty()) {
            throw new ZenException("DateTime input cannot be null or empty");
        }

        parseInput(input.trim());
    }

    /**
     * Attempts to parse the input string into LocalDateTime, LocalDate, or falls back to String
     */
    private void parseInput(String input) {
        // First try to parse as LocalDateTime (with time)
        try {
            this.dateTime = LocalDateTime.parse(input);
            this.type = DateTimeType.DATE_TIME;
            return;
        } catch (DateTimeParseException e) {
            // Ignore and try next format
        }

        // Try to parse as LocalDate (date only)
        try {
            this.date = LocalDate.parse(input);
            this.type = DateTimeType.DATE_ONLY;
            return;
        } catch (DateTimeParseException e) {
            // Ignore and fall back to string
        }

        // Fall back to string storage
        this.stringValue = input;
        this.type = DateTimeType.STRING;
    }

    @Override
    public String toString() {
        switch (type) {
        case DATE_TIME:
            return dateTime.format(DATE_TIME_FORMATTER);
        case DATE_ONLY:
            return date.format(DATE_FORMATTER);
        case STRING:
        default:
            return stringValue;
        }
    }


    /**
     * Enum to track which type of datetime is stored
     */
    public enum DateTimeType {
        DATE_TIME,  // LocalDateTime
        DATE_ONLY,  // LocalDate
        STRING      // String fallback
    }
}
