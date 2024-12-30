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
import usecases.EnsureEnoughStock;
import usecases.GetProductByNumber;
import usecases.GetProductsBySearch;

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
            Product product = new GetProductByNumber(stockReader).run(trimmedProductNumber);
            new EnsureEnoughStock().run(product, 1);

            // Display
            final String actionMessage = product.showDetails();
            product.setQuantity(1); // Shouldn't have to do this, but have to match original behaviour.
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
            List<Product> products = new GetProductsBySearch(stockReader).run(trimmedQuery);
            Product firstResult = products.getFirst();
            new EnsureEnoughStock().run(firstResult, 1);

            // Display
            final String actionMessage = firstResult.showDetails();
            firstResult.setQuantity(1); // Require 1
            basket.add(firstResult); // Add to basket
            picture.setValue(firstResult.getPicture());
            fireAction(actionMessage);
        } catch (ProductOutOfStockException e) {
            fireAction(e.getMessage() + " not in stock");
        } catch (ProductDoesNotExistException _) {
            fireAction("No results for \"" + trimmedQuery + "\"");
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
