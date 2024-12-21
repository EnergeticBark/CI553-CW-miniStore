package clients.customer;

import catalogue.Basket;
import catalogue.BetterBasket;
import catalogue.Product;
import debug.DEBUG;
import middle.MiddleFactory;
import middle.StockException;
import middle.StockReader;

import javax.swing.*;
import javax.swing.event.SwingPropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Implements the Model of the customer client
 */
public class CustomerModel {
    private final SwingPropertyChangeSupport pcs = new SwingPropertyChangeSupport(this);

    private Basket basket = null; // Bought items
    private StockReader stockReader = null;
    private ImageIcon picture = null;

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

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    // Tell the CustomerView that the model has changed, so it needs to redraw.
    private void fireAction(String actionMessage) {
        this.pcs.firePropertyChange("action", null, actionMessage);
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
            if (!stockReader.exists(trimmedProductNumber)) { // Product doesn't exist?
                fireAction("Unknown product number " + trimmedProductNumber);
                return;
            }

            Product product = stockReader.getDetails(trimmedProductNumber);
            if (product.getQuantity() < 1) { // Out of stock?
                fireAction(product.getDescription() + " not in stock");
                return;
            }

            // Display
            final String actionMessage = String.format(
                    "%s : %7.2f (%2d) ",
                    product.getDescription(),
                    product.getPrice(),
                    product.getQuantity()
            );
            product.setQuantity(1); // Require 1
            basket.add(product); // Add to basket
            picture = stockReader.getImage(trimmedProductNumber);
            fireAction(actionMessage);
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
            picture = stockReader.getImage(pr.getProductNum());
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
        picture = null; // No picture.
        fireAction("Enter Product Number");
    }

    /**
     * Return a picture of the product
     * @return An instance of an ImageIcon
     */
    public ImageIcon getPicture() {
        return picture;
    }

    /**
     * Make a new Basket
     * @return an instance of a new Basket
     */
    protected Basket makeBasket() {
        return new BetterBasket();
    }
}
