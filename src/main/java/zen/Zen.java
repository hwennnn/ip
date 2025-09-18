package zen;

import zen.command.CommandExecutor;
import zen.exception.ZenException;
import zen.storage.Storage;
import zen.task.TaskList;
import zen.ui.GuiResponseFormatter;
import zen.ui.Ui;



/**
 * Main class for the Zen task management chatbot.
 * 
 * This class serves as the core orchestrator for the Zen application,
 * managing the interaction between the UI, storage, task management, and command processing components.
 * It supports both console and GUI modes of operation.
 * 
 * Key responsibilities:
 * - Initialize and coordinate all major components (UI, Storage, TaskList, CommandExecutor)
 * - Handle application lifecycle (startup, main loop, shutdown)
 * - Provide unified interface for both console and GUI interactions
 * - Manage error handling and recovery during initialization
 */
public class Zen {

    private final Storage storage;
    private final Ui ui;
    private TaskList tasks;
    private CommandExecutor commandExecutor;

    /**
     * Constructs a Zen instance with default data file location.
     * 
     * Initializes all core components with proper error handling.
     * If the data file cannot be loaded, creates a new empty task list to ensure
     * the application remains functional.
     *
     * @throws RuntimeException if critical components fail to initialize (rare)
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
        commandExecutor = new CommandExecutor(tasks, storage, ui);
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

                if (commandExecutor.executeCommand(fullCommand)) {
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
     * Generates a response for the user's chat message in GUI mode.
     * 
     * This method provides the main interface for GUI interactions.
     * It processes user input through the command executor and returns formatted
     * responses suitable for display in the chat interface.
     *
     * @param input the user's input command (can be any valid Zen command)
     * @return formatted response string for display in the GUI
     * @see CommandExecutor#executeCommandForGui(String)
     */
    public String getResponse(String input) {
        try {
            return commandExecutor.executeCommandForGui(input);
        } catch (ZenException e) {
            return GuiResponseFormatter.formatError(e.getMessage());
        }
    }

}
