package zen;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    // Source: https://unsplash.com/photos/a-man-standing-in-front-of-a-purple-wall-bYODySpLIhE
    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.jpeg"));

    // Source: https://unsplash.com/photos/a-green-rectangle-with-white-text-on-it-su4Y9-LWE50
    private final Image zenImage = new Image(this.getClass().getResourceAsStream("/images/DaZen.jpg"));

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;
    private Zen zen;

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());

        // AI-Enhanced: Add keyboard shortcuts for better UX
        setupKeyboardShortcuts();
    }

    /**
     * AI-Generated: Sets up keyboard shortcuts for improved user experience.
     * - Enter: Send message
     * - Ctrl+L: Clear chat history
     */
    private void setupKeyboardShortcuts() {
        userInput.setOnKeyPressed(this::handleKeyPressed);
    }

    /**
     * AI-Generated: Handles keyboard shortcuts.
     * Note: Enter key is handled by TextField's onAction, so only handle other shortcuts here
     * @param event the keyboard event
     */
    @FXML
    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.L && event.isControlDown()) {
            clearChatHistory();
            event.consume();
        }
    }

    /**
     * AI-Generated: Clears the chat history and shows welcome message.
     */
    private void clearChatHistory() {
        dialogContainer.getChildren().clear();
        showWelcomeMessage();
    }

    /**
     * Injects the Zen instance
     */
    public void setZen(Zen z) {
        this.zen = z;
        showWelcomeMessage();
    }
    
    /**
     * Shows the welcome message when the application starts
     */
    private void showWelcomeMessage() {
        String welcomeMessage = "Hello! I'm Zen\nWhat can I do for you?";
        dialogContainer.getChildren().add(
                DialogBox.getDukeDialog(welcomeMessage, zenImage)
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Zen's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();

        if (input == null) {
            return; // Guard against null input
        }

        input = input.trim(); // Remove leading/trailing whitespace

        String response = this.zen.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getDukeDialog(response, zenImage)
        );
        userInput.clear();
        
        // Handle bye command by closing the application
        if (input.trim().equalsIgnoreCase("bye")) {
            // Add a small delay to allow the goodbye message to be displayed
            javafx.application.Platform.runLater(() -> {
                try {
                    Thread.sleep(1000); // 1 second delay
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                javafx.application.Platform.exit();
            });
        }
    }
}
