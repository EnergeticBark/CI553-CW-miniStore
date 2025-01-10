import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import products.Product;

import java.util.List;

/**
 * Implements the Customer view.
 */
public class BackDoorView {
    // Width and height of the window in pixels.
    private static final int WIDTH = 500;
    private static final int HEIGHT = 340;

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

        TableColumn<Product, String> productNumberColumn = new TableColumn<>("Product Number");
        productNumberColumn.setCellValueFactory(product -> {
            int productNumber = product.getValue().getProductNumber();
            String formattedProductNumber = String.format("%04d", productNumber);
            return new ReadOnlyObjectWrapper<>(formattedProductNumber);
        });
        productNumberColumn.setResizable(false);
        productNumberColumn.setPrefWidth(110);

        TableColumn<Product, Integer> productQuantityColumn = new TableColumn<>("Qty. In Stock");
        productQuantityColumn.setCellValueFactory(product -> new ReadOnlyObjectWrapper<>(product.getValue().getQuantity()));
        productQuantityColumn.setResizable(false);
        productQuantityColumn.setPrefWidth(100);

        ObservableList<Product> fakeList = FXCollections.observableList(
                List.of(
                        new Product(1, "", 0, 90),
                        new Product(2, "", 0, 20),
                        new Product(3, "", 0, 33),
                        new Product(4, "", 0, 10),
                        new Product(5, "", 0, 17),
                        new Product(6, "", 0, 15),
                        new Product(7, "", 0, 1),
                        new Product(8, "", 0, 32),
                        new Product(9, "", 0, 63)
                )
        );

        TableView<Product> productTable = new TableView<>();
        productTable.setItems(fakeList);
        productTable.getColumns().add(productNumberColumn);
        productTable.getColumns().add(productQuantityColumn);
        VBox.setVgrow(productTable, Priority.ALWAYS);

        productTable.setPrefWidth(226);

        Label productDescription = new Label("Panasonic Blu-ray");
        productDescription.setId("product-description");

        Label productPrice = new Label("$99.00");
        productPrice.setId("product-price");

        Label productNumber = new Label("Product#: 0009");
        productDescription.setId("product-description");

        VBox productPane = new VBox(productDescription, productPrice, productNumber);

        TitledPane selectedPane = new TitledPane("Selected Product", productPane);
        selectedPane.collapsibleProperty().setValue(false);
        VBox.setVgrow(selectedPane, Priority.ALWAYS);

        Label productQuantity = new Label("Quantity In Stock: (63)");
        productQuantity.setId("product-quantity");

        Label plusLabel = new Label("+");
        plusLabel.setId("plus-label");

        Spinner<Integer> spinner = new Spinner<>(0, 99999 , 1);
        spinner.setEditable(true);
        spinner.setId("spinner");

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button restockButton = new Button("Restock");

        HBox inputPane = new HBox(plusLabel, spinner, spacer, restockButton);

        VBox quantityPane = new VBox(productQuantity, inputPane);
        quantityPane.setSpacing(10);

        TitledPane stockPane = new TitledPane("Modify Stock", quantityPane);
        stockPane.collapsibleProperty().setValue(false);

        VBox rightPane = new VBox(selectedPane, stockPane);
        HBox.setHgrow(rightPane, Priority.ALWAYS);

        HBox hBox = new HBox(productTable, new Separator(), rightPane);

        Scene scene = new Scene(hBox);
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
        stage.setMinWidth(WIDTH);
        stage.setMinHeight(HEIGHT);
        scene.getStylesheets().add("css/backdoor.css");
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