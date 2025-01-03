import catalogue.Basket;
import catalogue.BetterBasket;
import catalogue.Product;
import debug.DEBUG;
import exceptions.ProductDoesNotExistException;
import javafx.beans.property.SimpleStringProperty;
import middle.DAO;
import middle.MiddleFactory;
import middle.StockException;
import usecases.GetProductByNumber;
import usecases.RestockProduct;

/**
 * Implements the Model of the back door client
 */
public class BackDoorModel {
    private Basket theBasket = null; // Bought items

    private DAO<Product> stockDAO = null;

    final SimpleStringProperty action = new SimpleStringProperty();
    final SimpleStringProperty output = new SimpleStringProperty();

    /*
     * Construct the model of the back door client
     * @param mf The factory to create the connection objects
     */
    public BackDoorModel(MiddleFactory mf) {
        try {
            stockDAO = mf.makeStockDAO(); // Database access
        } catch (Exception e) {
            DEBUG.error("CustomerModel.constructor\n%s", e.getMessage());
        }

        theBasket = makeBasket(); // Initial Basket
    }

    // Tell the BackDoorView that the model has changed, so it needs to redraw.
    private void fireAction(String actionMessage) {
        this.action.setValue(actionMessage);
        this.output.setValue(getBasket().getDetails());
    }

    /**
     * Get the Basket of products
     * @return basket
     */
    public Basket getBasket() {
        return theBasket;
    }

    /**
     * Query
     * @param productNumber The product number of the item
     */
    public void query(String productNumber) {
        String trimmedProductNumber = productNumber.trim();
        try {
            Product product = new GetProductByNumber(stockDAO).run(trimmedProductNumber);
            fireAction(product.showDetails());
        } catch (ProductDoesNotExistException e) {
            fireAction("Unknown product number " + trimmedProductNumber);
        } catch (StockException e) {
            fireAction(e.getMessage());
        }
    }

    /**
     * Re stock
     * @param productNumber The product number of the item
     * @param quantity How many to be added
     */
    public void restock(String productNumber, String quantity) {
        theBasket = makeBasket();
        String trimmedProductNumber = productNumber.trim();
        String trimmedQuantity = quantity.trim();

        try {
            int amount = Integer.parseUnsignedInt(trimmedQuantity);
            Product product = new RestockProduct(stockDAO).run(trimmedProductNumber, amount);

            theBasket.add(product);
            fireAction("");
        } catch (NumberFormatException e) {
            fireAction("Invalid quantity");
        } catch (ProductDoesNotExistException _) {
            fireAction("Unknown product number " + trimmedProductNumber);
        } catch (StockException e) {
            fireAction(e.getMessage());
        }
    }

    /**
     * Clear the product()
     */
    public void clear() {
        theBasket.clear(); // Clear s. list
        fireAction("Enter Product Number"); // Set display
    }

    /**
     * return an instance of a Basket
     * @return a new instance of a Basket
     */
    protected Basket makeBasket() {
        return new BetterBasket();
    }
}

