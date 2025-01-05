import products.Product;
import products.exceptions.ProductDoesNotExistException;
import javafx.beans.property.SimpleStringProperty;
import products.ProductDAO;
import middle.MiddleFactory;
import middle.DAOException;
import products.usecases.GetProductByNumber;
import products.usecases.GetProductsBySearch;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Implements the Model of the customer client
 */
public class CustomerModel {
    private Product product = null;
    private ProductDAO stockDAO = null;

    final SimpleStringProperty searchQuery = new SimpleStringProperty();

    final SimpleStringProperty picture = new SimpleStringProperty();
    final SimpleStringProperty productDescription = new SimpleStringProperty();
    final SimpleStringProperty productPrice = new SimpleStringProperty();
    final SimpleStringProperty productQuantity = new SimpleStringProperty();
    final SimpleStringProperty productNumber = new SimpleStringProperty();

    private static final System.Logger LOGGER = System.getLogger(CustomerModel.class.getName());

    /*
     * Construct the model of the Customer
     * @param mf The factory to create the connection objects
     */
    public CustomerModel(MiddleFactory mf) {
        try {
            stockDAO = mf.makeStockDAO(); // Database access
        } catch (Exception e) {
            LOGGER.log(System.Logger.Level.ERROR, "Database not created?\n{0}", e.getMessage());
            System.exit(-1);
        }
    }

    // Tell the CustomerView that the model has changed, so it needs to redraw.
    private void fireAction(String actionMessage) {
        if (product == null) {
            picture.setValue(null);
            productDescription.setValue(actionMessage);
            productPrice.setValue("");
            productQuantity.setValue("");
            productNumber.setValue("");
            return;
        }
        picture.setValue(product.getPicture());
        productDescription.setValue(product.getDescription());
        productPrice.setValue(
                NumberFormat.getCurrencyInstance(Locale.UK).format(
                        product.getPrice()
                )
        );
        productQuantity.setValue(String.format("Qty. In Stock: (%d)", product.getQuantity()));
        productNumber.setValue(String.format("Product#: %s", product.getProductNumber()));
    }

    /**
     * Search for a product by its product number or description.
     * @param searchQuery The search query.
     */
    public void search(String searchQuery) {
        product = null;
        final String trimmedQuery = searchQuery.trim();
        try {
            product = new GetProductByNumber(stockDAO).run(trimmedQuery);
            fireAction("");
            return;
        } catch (DAOException e) {
            LOGGER.log(System.Logger.Level.ERROR, e);
            System.exit(-1);
        } catch (ProductDoesNotExistException _) {}

        try {
            List<Product> products = new GetProductsBySearch(stockDAO).run(trimmedQuery);
            product = products.getFirst(); // Show the first search result.
            fireAction("");
        } catch (ProductDoesNotExistException _) {
            fireAction("No results for \"" + trimmedQuery + "\"");
        } catch (DAOException e) {
            LOGGER.log(System.Logger.Level.ERROR, e);
            System.exit(-1);
        }
    }

    /**
     * Clear the product
     */
    public void clear() {
        searchQuery.setValue("");
        product = null; // Clear the product.
        fireAction("Enter Product Number");
    }
}
