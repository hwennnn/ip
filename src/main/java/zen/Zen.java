package zen;

import zen.command.CommandExecutor;
import zen.exception.ZenException;
import zen.storage.Storage;
import zen.task.TaskList;
import zen.ui.GuiResponseFormatter;
import zen.ui.Ui;



/**
 * Main class for the Zen task management chatbot
 */
public class Zen {

    private final Storage storage;
    private final Ui ui;
    private TaskList tasks;
    private CommandExecutor commandExecutor;

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
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        try {
            return commandExecutor.executeCommandForGui(input);
        } catch (ZenException e) {
            return GuiResponseFormatter.formatError(e.getMessage());
        }
    }

}
