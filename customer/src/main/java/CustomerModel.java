import catalogue.Basket;
import catalogue.BetterBasket;
import catalogue.Product;
import debug.DEBUG;
import exceptions.ProductDoesNotExistException;
import exceptions.ProductOutOfStockException;
import javafx.beans.property.SimpleStringProperty;
import middle.MiddleFactory;
import middle.StockException;
import middle.StockReader;
import usecases.GetStockIfAvailable;

import java.util.List;

/**
 * Implements the Model of the customer client
 */
public class CustomerModel {
    private Basket basket = null; // Bought items
    private StockReader stockReader = null;

    final SimpleStringProperty picture = new SimpleStringProperty();
    final SimpleStringProperty action = new SimpleStringProperty();
    final SimpleStringProperty output = new SimpleStringProperty();

    /*
     * Construct the model of the Customer
     * @param mf The factory to create the connection objects
     */
    public CustomerModel(MiddleFactory mf) {
        try {
            stockReader = mf.makeStockReader(); // Database access
        } catch (Exception e) {
            DEBUG.error("""
                    CustomerModel.constructor
                    Database not created?
                    %s
                    """, e.getMessage());
        }
        basket = makeBasket(); // Initial Basket
    }

    // Tell the CustomerView that the model has changed, so it needs to redraw.
    private void fireAction(String actionMessage) {
        this.action.setValue(actionMessage);
        this.output.setValue(getBasket().getDetails());
    }

    /**
     * @return the basket of products
     */
    public Basket getBasket() {
        return basket;
    }

    /**
     * Check if the product is in Stock
     * @param productNumber The product number
     */
    public void checkStock(String productNumber) {
        basket.clear(); // Clear stock list.
        final String trimmedProductNumber = productNumber.trim(); // Product no.
        try {
            Product product = new GetStockIfAvailable(stockReader).run(trimmedProductNumber, 1);

            // Display
            final String actionMessage = String.format(
                    "%s : %7.2f (%2d) ",
                    product.getDescription(),
                    product.getPrice(),
                    product.getQuantity()
            );
            basket.add(product); // Add to basket
            picture.setValue(product.getPicture());
            fireAction(actionMessage);
        } catch (ProductOutOfStockException e) {
            fireAction(e.getMessage() + " not in stock");
        } catch (ProductDoesNotExistException _) {
            fireAction("Unknown product number " + trimmedProductNumber);
        } catch (StockException e) {
            DEBUG.error("CustomerClient.doCheck()\n%s", e.getMessage());
        }
    }

    /**
     * Search for a product by its description.
     * @param searchQuery The search query.
     */
    public void search(String searchQuery) {
        basket.clear(); // Clear stock list.
        final String trimmedQuery = searchQuery.trim();
        try {
            List<Product> products = stockReader.searchByDescription(trimmedQuery);
            if (products.isEmpty()) { // No search results?
                fireAction("No results for \"" + searchQuery + "\"");
                return;
            }

            Product pr = products.getFirst();
            if (pr.getQuantity() < 1) { // Out of stock?
                fireAction(pr.getDescription() + " not in stock");
                return;
            }

            // Display
            final String actionMessage = String.format(
                    "%s : %7.2f (%2d) ",
                    pr.getDescription(),
                    pr.getPrice(),
                    pr.getQuantity()
            );
            pr.setQuantity(1); // Require 1
            basket.add(pr); // Add to basket
            picture.setValue(pr.getPicture());
            fireAction(actionMessage);
        } catch (StockException e) {
            DEBUG.error("CustomerClient.search()\n%s", e.getMessage());
        }
    }

    /**
     * Clear the products from the basket
     */
    public void clear() {
        basket.clear(); // Clear stock list.
        picture.setValue(null); // No picture.
        fireAction("Enter Product Number");
    }

    /**
     * Make a new Basket
     * @return an instance of a new Basket
     */
    protected Basket makeBasket() {
        return new BetterBasket();
    }
}
