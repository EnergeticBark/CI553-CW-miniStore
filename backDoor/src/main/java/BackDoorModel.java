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
public class BackDoorModel {
    private Product product = null;
    private ProductDAO stockDAO = null;

    final SimpleObjectProperty<SpinnerValueFactory<Integer>> selectedQuantity = new SimpleObjectProperty<>(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99999, 0)
    );

    final ObservableList<Product> stockList = FXCollections.observableList(new ArrayList<>());
    final ObjectProperty<SortedList<Product>> sortedStockList = new SimpleObjectProperty<>(new SortedList<>(stockList));

    final SimpleStringProperty productDescription = new SimpleStringProperty();
    final SimpleStringProperty productPrice = new SimpleStringProperty();
    final SimpleStringProperty productQuantity = new SimpleStringProperty();
    final SimpleStringProperty productNumber = new SimpleStringProperty();

    final SimpleStringProperty action = new SimpleStringProperty();

    private static final System.Logger LOGGER = System.getLogger(BackDoorModel.class.getName());

    /*
     * Construct the model of the back door client
     * @param mf The factory to create the connection objects
     */
    public BackDoorModel(MiddleFactory mf) {
        try {
            stockDAO = mf.makeStockDAO(); // Database access
            stockList.setAll(stockDAO.getAll());
        } catch (Exception e) {
            LOGGER.log(System.Logger.Level.ERROR, e);
            System.exit(-1);
        }
    }

    // Tell the BackDoorView that the model has changed, so it needs to redraw.
    private void fireAction(String actionMessage) {
        action.setValue(actionMessage);
        if (product == null) {
            productDescription.setValue("");
            productPrice.setValue("");
            productQuantity.setValue("");
            productNumber.setValue("");
            return;
        }
        productDescription.setValue(product.getDescription());
        productPrice.setValue(
                NumberFormat.getCurrencyInstance(Locale.UK).format(
                        product.getPrice()
                )
        );
        productNumber.setValue(String.format("Product#: %04d", product.getProductNumber()));
        productQuantity.setValue(String.format("Quantity In Stock: (%d)", product.getQuantity()));
    }

    public void selectProduct(Product product) {
        this.product = product;
        fireAction("");
    }

    /**
     * Restock
     */
    public void restock() {
        if (product == null) {
            fireAction("No product selected.");
            return;
        }

        try {
            product = new RestockProduct(stockDAO).run(product.getProductNumber(), selectedQuantity.get().getValue());
            fireAction("");
            try {
                // Refresh the stock list.
                stockList.setAll(stockDAO.getAll());
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
            selectedQuantity.get().setValue(0);
        } catch (ProductDoesNotExistException | NumberFormatException _) {
            fireAction("Unknown product number " + product.getProductNumber());
        } catch (DAOException e) {
            fireAction(e.getMessage());
        }
    }
}
