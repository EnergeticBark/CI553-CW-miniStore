package clients.customer;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Implements the Customer view.
 */
public class CustomerView {
    // Width and height of the window in pixels.
    private static final int WIDTH = 420;
    private static final int HEIGHT = 270;

    // Width and height of the button in pixels.
    private static final int BUTTON_WIDTH = 80;
    private static final int BUTTON_HEIGHT = 35;

    private final TextField inputField = new TextField();

    private CustomerController controller = null;

    /**
     * Construct the view
     * @param stage   Window in which to construct
     * @param x     x-coordinate of position of window on screen
     * @param y     y-coordinate of position of window on screen
     */
    public CustomerView(Stage stage, CustomerModel model, int x, int y) {
        // Set window location.
        stage.setX(x);
        stage.setY(y);

        HBox hBox = new HBox(
                makeLeftPane(model),
                makeRightPane(model)
        );
        hBox.setPadding(new Insets(0, 16, 0, 16));
        hBox.setSpacing(16);

        // Create scene, specifying the size of the window.
        Scene scene = new Scene(hBox, WIDTH, HEIGHT);
        stage.setScene(scene);

        inputField.requestFocus();
        stage.show();
    }

    private VBox makeLeftPane(CustomerModel model) {
        Button checkButton = new Button("Check");
        checkButton.setOnAction(_ -> controller.checkStock(inputField.getText()));
        checkButton.setMinSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
        checkButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        Button searchButton = new Button("Search");
        searchButton.setOnAction(_ -> controller.search(inputField.getText()));
        searchButton.setMinSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
        searchButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        Button clearButton = new Button("Clear");
        clearButton.setOnAction(_ -> controller.clear());
        clearButton.setMinSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
        clearButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        final ImageView picture = new ImageView();
        model.picture.addListener((_, _, image) -> {
            if (image == null) {
                picture.setImage(null); // Clear picture
            } else {
                picture.setImage(new Image(image)); // Display picture
            }
        });
        final Pane pictureFrame = new Pane(picture);
        pictureFrame.setStyle("-fx-background-color: white;");
        pictureFrame.setMinSize(80, 80);

        VBox vBox = new VBox(checkButton, searchButton, clearButton, pictureFrame);
        vBox.setSpacing(16);
        vBox.setPadding(new Insets(25, 0, 25, 0));
        return vBox;
    }

    private VBox makeRightPane(CustomerModel model) {
        final Label pageTitle = new Label("Search products");
        final Label actionLabel = new Label();
        actionLabel.textProperty().bind(model.action);
        final TextArea outputText = new TextArea();
        outputText.textProperty().bind(model.output);
        outputText.setFont(Font.font("Monospaced", 12));

        VBox vBox = new VBox(pageTitle, actionLabel, inputField, outputText);
        vBox.setSpacing(10);
        return vBox;
    }

    /**
     * The controller object, used so that an interaction can be passed to the controller
     * @param controller   The controller
     */
    public void setController(CustomerController controller) {
        this.controller = controller;
    }
}
