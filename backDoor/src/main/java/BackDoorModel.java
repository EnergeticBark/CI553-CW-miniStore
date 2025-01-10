import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import products.Product;
import products.exceptions.ProductDoesNotExistException;
import javafx.beans.property.SimpleStringProperty;
import products.ProductDAO;
import middle.MiddleFactory;
import dao.DAOException;
import products.usecases.RestockProduct;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Implements the Model of the back door client
 */
public class BackDoorModel {
    private Product product = null;

    final ObjectProperty<ObservableList<Product>> stockList = new SimpleObjectProperty<>();

    final SimpleStringProperty productDescription = new SimpleStringProperty();
    final SimpleStringProperty productPrice = new SimpleStringProperty();
    final SimpleStringProperty productQuantity = new SimpleStringProperty();
    final SimpleStringProperty productNumber = new SimpleStringProperty();

    private ProductDAO stockDAO = null;

    final SimpleStringProperty action = new SimpleStringProperty();

    private static final System.Logger LOGGER = System.getLogger(BackDoorModel.class.getName());

    /*
     * Construct the model of the back door client
     * @param mf The factory to create the connection objects
     */
    public BackDoorModel(MiddleFactory mf) {
        try {
            stockDAO = mf.makeStockDAO(); // Database access
            stockList.setValue(FXCollections.observableList(stockDAO.getAll()));
        } catch (Exception e) {
            LOGGER.log(System.Logger.Level.ERROR, e);
            System.exit(-1);
        }
    }

    // Tell the BackDoorView that the model has changed, so it needs to redraw.
    private void fireAction(String actionMessage) {
        this.action.setValue(actionMessage);

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
     * Re stock
     * @param quantity How many to be added
     */
    public void restock(int quantity) {
        if (product == null) {
            // TODO show this in the UI instead.
            throw new RuntimeException("No product selected.");
        }

        try {
            product = new RestockProduct(stockDAO).run(product.getProductNumber(), quantity);
            fireAction("");
            try {
                // Refresh the stock list.
                stockList.setValue(FXCollections.observableList(stockDAO.getAll()));
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
        } catch (ProductDoesNotExistException | NumberFormatException _) {
            fireAction("Unknown product number " + product.getProductNumber());
        } catch (DAOException e) {
            fireAction(e.getMessage());
        }
    }
}
