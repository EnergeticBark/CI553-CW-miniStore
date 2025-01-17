import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import products.Product;

/**
 * Implements the Customer view.
 */
class BackDoorView {
    // Width and height of the window in pixels.
    private static final int WIDTH = 500;
    private static final int HEIGHT = 340;

    private BackDoorController controller = null;

    /**
     * Construct the view
     * @param x x-coordinate of position of window on screen
     * @param y y-coordinate of position of window on screen
     */
    BackDoorView(Stage stage, BackDoorModel model, int x, int y) {
        // Set window location.
        stage.setX(x);
        stage.setY(y);

        HBox hBox = new HBox(makeProductTable(model), new Separator(), makeRightPane(model));

        Scene scene = new Scene(hBox);
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
        stage.setMinWidth(WIDTH);
        stage.setMinHeight(HEIGHT);
        scene.getStylesheets().add("css/backdoor.css");
        stage.setScene(scene);

        stage.show();
    }

    private TableView<Product> makeProductTable(BackDoorModel model) {
        TableColumn<Product, String> productNumberColumn = new TableColumn<>("Product Number");
        productNumberColumn.setCellValueFactory(product -> {
            int productNumber = product.getValue().getProductNumber();
            String formattedProductNumber = String.format("%04d", productNumber);
            return new ReadOnlyObjectWrapper<>(formattedProductNumber);
        });
        productNumberColumn.setResizable(false);
        productNumberColumn.setId("number-column");

        TableColumn<Product, Integer> productQuantityColumn = new TableColumn<>("Qty. In Stock");
        productQuantityColumn.setCellValueFactory(product -> new ReadOnlyObjectWrapper<>(product.getValue().getQuantity()));
        productQuantityColumn.setResizable(false);
        productQuantityColumn.setId("quantity-column");

        TableView<Product> productTable = new TableView<>();
        productTable.itemsProperty().bind(model.sortedStockList);
        productTable.getColumns().add(productNumberColumn);
        productTable.getColumns().add(productQuantityColumn);
        productTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((_, _, product) -> controller.selectProduct(product));
        model.sortedStockList.get().comparatorProperty().bind(productTable.comparatorProperty());
        VBox.setVgrow(productTable, Priority.ALWAYS);
        productTable.setId("product-table");
        return productTable;
    }

    private VBox makeRightPane(BackDoorModel model) {
        Label productDescription = new Label();
        productDescription.textProperty().bind(model.productDescription);
        productDescription.setId("product-description");

        Label productPrice = new Label();
        productPrice.textProperty().bind(model.productPrice);
        productPrice.setId("product-price");

        Label productNumber = new Label();
        productNumber.textProperty().bind(model.productNumber);
        productDescription.setId("product-description");

        VBox productPane = new VBox(productDescription, productPrice, productNumber);

        TitledPane selectedPane = new TitledPane("Selected Product", productPane);
        VBox.setVgrow(selectedPane, Priority.ALWAYS);

        Label productQuantity = new Label();
        productQuantity.textProperty().bind(model.productQuantity);
        productQuantity.setId("product-quantity");

        Label plusLabel = new Label("+");
        plusLabel.setId("plus-label");

        Spinner<Integer> spinner = new Spinner<>(0, 99999 , 0);
        spinner.setEditable(true);
        spinner.valueFactoryProperty().bind(model.selectedQuantity);
        spinner.setId("spinner");

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button restockButton = new Button("Restock");
        restockButton.setOnAction(_ -> controller.restock());

        HBox inputPane = new HBox(plusLabel, spinner, spacer, restockButton);

        VBox quantityPane = new VBox(productQuantity, inputPane);
        quantityPane.setId("quantity-pane");

        TitledPane stockPane = new TitledPane("Modify Stock", quantityPane);

        Label actionLabel = new Label();
        actionLabel.textProperty().bind(model.action);

        VBox rightPane = new VBox(selectedPane, stockPane, actionLabel);
        HBox.setHgrow(rightPane, Priority.ALWAYS);
        return rightPane;
    }

    public void setController(BackDoorController controller) {
        this.controller = controller;
    }
}