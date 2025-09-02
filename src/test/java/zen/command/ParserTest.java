package zen.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * JUnit tests for the Parser class
 */
public class ParserTest {

    @Test
    public void parseCommand_basicCommands_returnsCorrectCommandType() {
        // Test basic commands
        assertEquals(Parser.CommandType.BYE, Parser.parseCommand("bye"));
        assertEquals(Parser.CommandType.LIST, Parser.parseCommand("list"));
        assertEquals(Parser.CommandType.TODO, Parser.parseCommand("todo read book"));
        assertEquals(Parser.CommandType.MARK, Parser.parseCommand("mark 1"));
        assertEquals(Parser.CommandType.UNKNOWN, Parser.parseCommand("invalid"));
        assertEquals(Parser.CommandType.EMPTY, Parser.parseCommand(null));
    }

    @Test
    public void parseTaskIndex_validAndInvalidInputs_returnsCorrectResult() {
        // Test valid indices (converts from 1-based to 0-based)
        assertEquals(0, Parser.parseTaskIndex("mark 1", "mark "));
        assertEquals(4, Parser.parseTaskIndex("delete 5", "delete "));

        // Test invalid inputs
        assertEquals(-1, Parser.parseTaskIndex("mark abc", "mark "));
        assertEquals(-1, Parser.parseTaskIndex("mark 0", "mark "));
        assertEquals(-1, Parser.parseTaskIndex("mark ", "mark "));
    }
}
