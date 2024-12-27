package clients.packing;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Implements the Packing view.
 */

public class PackingView {
    // Width and height of the window in pixels.
    private static final int WIDTH = 420;
    private static final int HEIGHT = 270;

    // Width and height of the button in pixels.
    private static final int BUTTON_WIDTH = 80;
    private static final int BUTTON_HEIGHT = 35;

    private PackingController controller = null;

    /**
     * Construct the view
     * @param stage Window in which to construct
     * @param x x-coordinate of position of window on screen
     * @param y y-coordinate of position of window on screen
     */
    public PackingView(Stage stage, PackingModel model, int x, int y) {
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
        Button packedButton = new Button("Packed");
        packedButton.setOnAction(_ -> controller.doPacked());
        packedButton.setMinSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
        packedButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        VBox vBox = new VBox(packedButton);
        vBox.setSpacing(16);
        vBox.setPadding(new Insets(25, 0, 25, 0));
        return vBox;
    }

    private VBox makeRightPane(PackingModel model) {
        Label pageTitle = new Label("Packing Bought Order");

        Label actionLabel = new Label();
        actionLabel.textProperty().bind(model.action);

        TextArea outputText = new TextArea();
        outputText.textProperty().bind(model.output);
        outputText.setFont(Font.font("Monospaced", 12));

        VBox vBox = new VBox(pageTitle, actionLabel, outputText);
        vBox.setSpacing(10);
        return vBox;
    }

    public void setController(PackingController controller) {
        this.controller = controller;
    }
}
