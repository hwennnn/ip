package zen.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import zen.exception.ZenException;

/**
 * JUnit tests for the TaskList class
 */
public class TaskListTest {
    private TaskList taskList;
    private Task task1;
    private Task task2;

    @BeforeEach
    public void setUp() throws ZenException {
        taskList = new TaskList();
        task1 = new Todo("Read book");
        task2 = new Todo("Write report");
    }

    @Test
    public void addTask_validTask_increasesSize() throws ZenException {
        assertEquals(0, taskList.size());
        taskList.addTask(task1);
        assertEquals(1, taskList.size());
        assertEquals("Read book", taskList.getTask(0).getDescription());
    }

    @Test
    public void deleteTask_validIndex_removesTask() throws ZenException {
        taskList.addTask(task1);
        taskList.addTask(task2);

        Task deletedTask = taskList.deleteTask(0);
        assertEquals(task1, deletedTask);
        assertEquals(1, taskList.size());
        assertEquals("Write report", taskList.getTask(0).getDescription());
    }

    @Test
    public void deleteTask_invalidIndex_throwsException() throws ZenException {
        taskList.addTask(task1);

        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.deleteTask(-1);
        });

        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.deleteTask(1);
        });
    }

    @Test
    public void markTask_validIndex_marksTaskAsDone() throws ZenException {
        taskList.addTask(task1);
        assertFalse(task1.isDone());

        taskList.markTask(0);
        assertTrue(task1.isDone());
    }

    @Test
    public void markTask_invalidIndex_throwsException() throws ZenException {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.markTask(0);
        });
    }
}