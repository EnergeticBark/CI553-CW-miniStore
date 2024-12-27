import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Implements the Customer view.
 */
public class BackDoorView {
    // Width and height of the window in pixels.
    private static final int WIDTH = 420;
    private static final int HEIGHT = 270;

    // Width and height of the button in pixels.
    private static final int BUTTON_WIDTH = 80;
    private static final int BUTTON_HEIGHT = 35;

    private final TextField inputField = new TextField();
    private final TextField inputField2 = new TextField();

    private BackDoorController controller = null;

    /**
     * Construct the view
     * @param x x-coordinate of position of window on screen
     * @param y y-coordinate of position of window on screen
     */
    public BackDoorView(Stage stage, BackDoorModel model, int x, int y) {
        // Set window location.
        stage.setX(x);
        stage.setY(y);

        HBox hBox = new HBox(
                makeLeftPane(),
                makeRightPane(model)
        );
        hBox.setPadding(new Insets(0, 16, 0, 16));
        hBox.setSpacing(16);

        Scene scene = new Scene(hBox, WIDTH, HEIGHT);
        stage.setScene(scene);

        stage.show();
    }

    private VBox makeLeftPane() {
        Button queryButton = new Button("Query");
        queryButton.setOnAction(_ -> controller.query(inputField.getText()));
        queryButton.setMinSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
        queryButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        Button restockButton = new Button("Restock");
        restockButton.setOnAction(_ -> controller.restock(inputField.getText(), inputField2.getText()));
        restockButton.setMinSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
        restockButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        Button clearButton = new Button("Clear");
        clearButton.setOnAction(_ -> controller.clear());
        clearButton.setMinSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
        clearButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        VBox vBox = new VBox(queryButton, restockButton, clearButton);
        vBox.setSpacing(16);
        vBox.setPadding(new Insets(25, 0, 25, 0));
        return vBox;
    }

    private VBox makeRightPane(BackDoorModel model) {
        Label pageTitle = new Label("Staff check and manage stock");

        Label actionLabel = new Label();
        actionLabel.textProperty().bind(model.action);

        TextArea outputText = new TextArea();
        outputText.textProperty().bind(model.output);
        outputText.setFont(Font.font("Monospaced", 12));

        HBox hBox = new HBox(inputField, inputField2);
        hBox.setSpacing(30);

        VBox vBox = new VBox(pageTitle, actionLabel, hBox, outputText);
        vBox.setSpacing(10);
        return vBox;
    }

    public void setController(BackDoorController controller) {
        this.controller = controller;
    }
}