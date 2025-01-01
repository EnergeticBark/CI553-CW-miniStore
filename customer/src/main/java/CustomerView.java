import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

/**
 * Implements the Customer view.
 */
public class CustomerView {
    // Width and height of the window in pixels.
    private static final int WIDTH = 500;
    private static final int HEIGHT = 360;

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

        Label title = new Label("Search by Keyword or Product Number.");
        title.setId("title");
        TextField searchBar = new TextField();
        searchBar.promptTextProperty().setValue("\"TV\" or \"0001\"");
        HBox.setHgrow(searchBar, Priority.ALWAYS);
        Button searchButton = new Button("Search");
        Button clearButton = new Button("Clear");
        HBox searchControls = new HBox(searchBar, searchButton, clearButton);
        searchControls.setId("search-controls");
        VBox searchPane = new VBox(title, searchControls);
        searchPane.setId("search-pane");

        Separator separator = new Separator();

        ImageView imageView = new ImageView(new Image("images/pic0001.jpg", 90, 90, false, false));
        Pane pictureFrame = new Pane(imageView);
        pictureFrame.setId("picture-frame");
        pictureFrame.setMinSize(80, 80);
        Label productTitle = new Label("Panasonic Blu-ray");
        productTitle.setId("product-title");
        Label productPrice = new Label("$99.00");
        productPrice.setId("product-price");
        Label productDescription = new Label("Panasonic Blu-ray ninty-nine dollars. H-H-H-H-H-H-H-H-gregg");
        productDescription.setId("product-description");
        Label productQuantity = new Label("Qty. In Stock: 63");
        Pane spacer = new Pane();
        spacer.setId("spacer");
        HBox.setHgrow(spacer, Priority.ALWAYS);
        productQuantity.setId("product-quantity");
        Label productNumber = new Label("Product#:  0009");
        HBox productFooter = new HBox(productQuantity, spacer, productNumber);
        productFooter.setId("product-footer");
        VBox productDetails = new VBox(productTitle, productPrice, productDescription, productFooter);
        HBox productPane = new HBox(pictureFrame, productDetails);
        productPane.setId("product-pane");

        VBox vBox = new VBox(searchPane, separator, productPane);


        /*HBox hBox = new HBox(
                makeLeftPane(model),
                makeRightPane(model)
        );
        hBox.setPadding(new Insets(0, 16, 0, 16));
        hBox.setSpacing(16);

        // Create scene, specifying the size of the window.
        */Scene scene = new Scene(vBox, WIDTH, HEIGHT);
        stage.setMinWidth(WIDTH);
        stage.setMinHeight(HEIGHT);
        scene.getStylesheets().add("css/customer.css");
        stage.setScene(scene);
        /*

        inputField.requestFocus();*/


        stage.show();
    }

    /*private VBox makeLeftPane(CustomerModel model) {
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
    }*/

    /**
     * The controller object, used so that an interaction can be passed to the controller
     * @param controller   The controller
     */
    public void setController(CustomerController controller) {
        this.controller = controller;
    }
}
