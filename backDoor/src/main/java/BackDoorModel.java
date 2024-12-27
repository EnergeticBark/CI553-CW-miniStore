import catalogue.Basket;
import catalogue.BetterBasket;
import catalogue.Product;
import debug.DEBUG;
import javafx.beans.property.SimpleStringProperty;
import middle.MiddleFactory;
import middle.StockException;
import middle.StockReadWriter;

/**
 * Implements the Model of the back door client
 */
public class BackDoorModel {
    private Basket theBasket = null; // Bought items

    private StockReadWriter theStock = null;

    final SimpleStringProperty action = new SimpleStringProperty();
    final SimpleStringProperty output = new SimpleStringProperty();

    /*
     * Construct the model of the back door client
     * @param mf The factory to create the connection objects
     */
    public BackDoorModel(MiddleFactory mf) {
        try {
            theStock = mf.makeStockReadWriter(); // Database access
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
            if (!theStock.exists(trimmedProductNumber)) { // Product doesn't exist?
                fireAction("Unknown product number " + trimmedProductNumber);
                return;
            }

            Product product = theStock.getDetails(trimmedProductNumber);
            fireAction(String.format("%s : %7.2f (%2d) ",
                    product.getDescription(),
                    product.getPrice(),
                    product.getQuantity()
            ));
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
        int amount;
        try {
            amount = Integer.parseUnsignedInt(trimmedQuantity);
        } catch (NumberFormatException e) {
            fireAction("Invalid quantity");
            return;
        }

        try {
            if (!theStock.exists(trimmedProductNumber)) { // Product doesn't exist?
                fireAction("Unknown product number " + trimmedProductNumber);
                return;
            }

            theStock.addStock(trimmedProductNumber, amount); // Re stock
            Product product = theStock.getDetails(trimmedProductNumber); // Get details
            theBasket.add(product);
            fireAction("");
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

