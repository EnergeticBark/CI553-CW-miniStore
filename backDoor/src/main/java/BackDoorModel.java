import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.SpinnerValueFactory;
import products.Product;
import products.exceptions.ProductDoesNotExistException;
import javafx.beans.property.SimpleStringProperty;
import products.ProductDAO;
import middle.MiddleFactory;
import dao.DAOException;
import products.usecases.RestockProduct;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Implements the Model of the back door client
 */
class BackDoorModel {
    private Product product = null; // The product currently selected to be restocked.
    private ProductDAO stockDAO = null;

    // The current value of the Spinner control, i.e. how much stock to add to the selected product.
    final SimpleObjectProperty<SpinnerValueFactory<Integer>> selectedQuantity = new SimpleObjectProperty<>(
            // Our spinner has a valid input range of 0-99999, and starts at 0 by default.
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99999, 0)
    );

    // The list of products to show in BackDoorView's product table.
    final ObservableList<Product> stockList = FXCollections.observableList(new ArrayList<>());
    final ObjectProperty<SortedList<Product>> sortedStockList = new SimpleObjectProperty<>(new SortedList<>(stockList));

    // String properties with info about the selected product.
    final SimpleStringProperty productDescription = new SimpleStringProperty();
    final SimpleStringProperty productPrice = new SimpleStringProperty();
    final SimpleStringProperty productQuantity = new SimpleStringProperty();
    final SimpleStringProperty productNumber = new SimpleStringProperty();

    // String property for the "action" label, used to communicate errors to the user.
    final SimpleStringProperty action = new SimpleStringProperty();

    private static final System.Logger LOGGER = System.getLogger(BackDoorModel.class.getName());

    /**
     * Construct the model of the back door client
     * @param mf The factory to create the connection objects
     */
    BackDoorModel(MiddleFactory mf) {
        try {
            stockDAO = mf.makeStockDAO(); // Database access
            stockList.setAll(stockDAO.getAll()); // Load a list of all products into BackDoorView's product table.
        } catch (Exception e) {
            LOGGER.log(System.Logger.Level.ERROR, e);
            System.exit(-1);
        }
    }

    /**
     * Tell the BackDoorView that the model has changed, so it needs to redraw.
     * @param actionMessage message to show the user
     */
    private void fireAction(String actionMessage) {
        action.setValue(actionMessage);
        if (product == null) {
            // If no product was selected, clear all the product string properties.
            productDescription.setValue("");
            productPrice.setValue("");
            productQuantity.setValue("");
            productNumber.setValue("");
            return;
        }
        // Otherwise, update the product string properties.
        productDescription.setValue(product.getDescription());
        productPrice.setValue(
                NumberFormat.getCurrencyInstance(Locale.UK).format(
                        product.getPrice()
                )
        );
        productNumber.setValue(String.format("Product#: %04d", product.getProductNumber()));
        productQuantity.setValue(String.format("Quantity In Stock: (%d)", product.getQuantity()));
    }

    /** Update the model's selected product */
    void selectProduct(Product product) {
        this.product = product;
        fireAction(""); // Update the view with no error message.
    }

    /**
     * Restock the selected product by the amount specified in the Spinner.
     */
    void restock() {
        if (product == null) {
            fireAction("No product selected.");
            return;
        }

        try {
            // Invoke the RestockProduct use case.
            product = new RestockProduct(stockDAO).run(product.getProductNumber(), selectedQuantity.get().getValue());
            fireAction(""); // Update the view with no error message.
            stockList.setAll(stockDAO.getAll()); // Refresh the list of products.
            selectedQuantity.get().setValue(0);
        } catch (ProductDoesNotExistException | NumberFormatException _) {
            fireAction("Unknown product number " + product.getProductNumber());
        } catch (DAOException e) {
            fireAction(e.getMessage());
        }
    }
}
