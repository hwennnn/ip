package zen;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private final Image dukeImage = new Image(this.getClass().getResourceAsStream("/images/DaDuke.png"));

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
                DialogBox.getDukeDialog(welcomeMessage, dukeImage)
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Zen's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = this.zen.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getDukeDialog(response, dukeImage)
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