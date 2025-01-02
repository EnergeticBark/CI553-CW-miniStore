import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Implements the Customer view.
 */
public class CustomerView {
    // Width and height of the window in pixels.
    private static final int WIDTH = 500;
    private static final int HEIGHT = 340;

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

        Separator separator = new Separator();

        VBox vBox = new VBox(makeSearchPane(model), separator, makeProductPane(model));

        // Create scene, specifying the size of the window.
        Scene scene = new Scene(vBox, WIDTH, HEIGHT);
        stage.setMinWidth(WIDTH);
        stage.setMinHeight(HEIGHT);
        scene.getStylesheets().add("css/customer.css");
        stage.setScene(scene);

        stage.show();
    }

    private VBox makeSearchPane(CustomerModel model) {
        Label title = new Label("Search by Keyword or Product Number.");
        title.setId("title");

        TextField searchBar = new TextField();
        searchBar.textProperty().bindBidirectional(model.searchQuery);
        searchBar.promptTextProperty().setValue("\"TV\" or \"0001\"");
        searchBar.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                controller.search(searchBar.getText());
            }
        });
        HBox.setHgrow(searchBar, Priority.ALWAYS);

        Button searchButton = new Button("Search");
        searchButton.setOnAction(_ -> controller.search(searchBar.getText()));

        Button clearButton = new Button("Clear");
        clearButton.setOnAction(_ -> controller.clear());

        HBox searchControls = new HBox(searchBar, searchButton, clearButton);
        searchControls.setId("search-controls");

        VBox searchPane = new VBox(title, searchControls);
        searchPane.setId("search-pane");
        return searchPane;
    }

    private HBox makeProductPane(CustomerModel model) {
        ImageView imageView = new ImageView();
        model.picture.addListener((_, _, image) -> {
            if (image == null) {
                imageView.setImage(null); // Clear picture
            } else {
                imageView.setImage(new Image(image, 90, 90, true, false)); // Display picture
            }
        });
        Pane pictureFrame = new Pane(imageView);
        pictureFrame.setId("picture-frame");

        Label productDescription = new Label();
        productDescription.textProperty().bind(model.productDescription);
        productDescription.setId("product-description");

        Label productPrice = new Label();
        productPrice.textProperty().bind(model.productPrice);
        productPrice.setId("product-price");

        Label productQuantity = new Label();
        productQuantity.textProperty().bind(model.productQuantity);

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label productNumber = new Label();
        productNumber.textProperty().bind(model.productNumber);

        HBox productFooter = new HBox(productQuantity, spacer, productNumber);
        productFooter.setId("product-footer");

        VBox productDetails = new VBox(productDescription, productPrice, productFooter);
        HBox.setHgrow(productDetails, Priority.ALWAYS);

        HBox productPane = new HBox(pictureFrame, productDetails);
        productPane.setId("product-pane");
        return productPane;
    }

    /**
     * The controller object, used so that an interaction can be passed to the controller
     * @param controller   The controller
     */
    public void setController(CustomerController controller) {
        this.controller = controller;
    }
}
