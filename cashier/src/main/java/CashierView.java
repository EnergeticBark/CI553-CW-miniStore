import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * View of the model 
 */
class CashierView {
    // Width and height of the window in pixels.
    private static final int WIDTH = 420;
    private static final int HEIGHT = 270;

    // Width and height of the button in pixels.
    private static final int BUTTON_WIDTH = 80;
    private static final int BUTTON_HEIGHT = 35;

    private final TextField inputField = new TextField();

    private CashierController controller = null;

    /**
     * Construct the view
     * @param stage Window in which to construct
     * @param x x-coordinate of position of window on screen
     * @param y y-coordinate of position of window on screen
     */
    CashierView(Stage stage, CashierModel model, int x, int y) {
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
        inputField.requestFocus();
        stage.show();
    }

    private VBox makeLeftPane() {
        Button checkButton = new Button("Check");
        checkButton.setOnAction(_ -> controller.checkStock(inputField.getText()));
        checkButton.setMinSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
        checkButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        Button buyButton = new Button("Buy");
        buyButton.setOnAction(_ -> controller.buy());
        buyButton.setMinSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
        buyButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        Button boughtButton = new Button("Bought/Pay");
        boughtButton.setOnAction(_ -> controller.bought());
        boughtButton.setMinSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
        boughtButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        VBox vBox = new VBox(checkButton, buyButton, boughtButton);
        vBox.setSpacing(16);
        vBox.setPadding(new Insets(25, 0, 25, 0));
        return vBox;
    }

    private VBox makeRightPane(CashierModel model) {
        Label pageTitle = new Label("Thank You for Shopping at MiniStore");

        Label actionLabel = new Label();
        actionLabel.textProperty().bind(model.action);

        TextArea outputText = new TextArea();

        outputText.textProperty().bind(model.output);
        outputText.setFont(Font.font("Monospaced", 12));

        VBox vBox = new VBox(pageTitle, actionLabel, inputField, outputText);
        vBox.setSpacing(10);
        return vBox;
    }

    /**
     * The controller object, used so that an interaction can be passed to the controller
     * @param controller the controller
     */
    void setController(CashierController controller) {
        this.controller = controller;
    }
}
