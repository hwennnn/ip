package zen.task;

/**
 * Enumeration representing the different types of tasks supported by the system.
 * Each task type has an associated symbol used for display and storage purposes.
 */
public enum TaskType {
    TODO("T"),
    DEADLINE("D"),
    EVENT("E");

    private final String symbol;

    /**
     * Constructs a TaskType with the specified symbol.
     *
     * @param symbol the single-character symbol representing this task type
     */
    TaskType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return "[" + symbol + "]";
    }
}
