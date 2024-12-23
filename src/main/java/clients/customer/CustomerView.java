package clients.customer;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Implements the Customer view.
 */
public class CustomerView implements PropertyChangeListener {
    // Width and height of the window in pixels.
    private static final int WIDTH = 420;
    private static final int HEIGHT = 280;

    private final ImageView picture = new ImageView();
    private final Label actionLabel = new Label();
    private final TextField inputField = new TextField();
    private final TextArea outputText = new TextArea();

    private CustomerController controller = null;

    /**
     * Construct the view
     * @param stage   Window in which to construct
     * @param x     x-coordinate of position of window on screen
     * @param y     y-coordinate of position of window on screen
     */
    public CustomerView(Stage stage, int x, int y) {
        // Set window location.
        stage.setX(x);
        stage.setY(y);

        // Check button.
        Button checkButton = new Button("Check");
        checkButton.setOnAction(_ -> controller.checkStock(inputField.getText()));
        checkButton.setPrefSize(80, 35);
        Button searchButton = new Button("Search");
        searchButton.setOnAction(_ -> controller.search(inputField.getText()));
        searchButton.setPrefSize(80, 35);
        Button clearButton = new Button("Clear");
        clearButton.setOnAction(_ -> controller.clear());
        clearButton.setPrefSize(80, 35);
        final Pane pictureFrame = new Pane(picture);
        pictureFrame.setStyle("-fx-background-color: white;");
        pictureFrame.setMinSize(80, 80);

        VBox leftVBox = new VBox();
        leftVBox.getChildren().addAll(checkButton, searchButton, clearButton, pictureFrame);
        leftVBox.setSpacing(16);
        leftVBox.setPadding(new Insets(25, 0, 25, 0));

        Label pageTitle = new Label("Search products");
        outputText.setFont(Font.font("Monospaced", 12));

        VBox rightVBox = new VBox();
        rightVBox.getChildren().addAll(pageTitle, actionLabel, inputField, outputText);
        rightVBox.setSpacing(10);

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(0, 16, 0, 16));
        hBox.setSpacing(16);
        hBox.getChildren().addAll(leftVBox, rightVBox);

        // Create scene, specifying the size of the window.
        Scene scene = new Scene(hBox, WIDTH, HEIGHT);
        stage.setScene(scene);

        inputField.requestFocus();
        stage.show();
    }

    /**
     * The controller object, used so that an interaction can be passed to the controller
     * @param controller   The controller
     */
    public void setController(CustomerController controller) {
        this.controller = controller;
    }

    /**
     * Update the view
     * @param evt The event source and property that has changed
     */
    public void propertyChange(PropertyChangeEvent evt) {
        CustomerModel model = (CustomerModel) evt.getSource();
        String message = (String) evt.getNewValue();
        actionLabel.setText(message);
        String image = model.getPicture(); // Image of product
        if (image == null) {
            picture.setImage(null); // Clear picture
        } else {
            picture.setImage(new Image(image)); // Display picture
        }
        outputText.setText(model.getBasket().getDetails());
        inputField.requestFocus(); // Focus is here
    }
}
