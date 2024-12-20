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

    /**
     * @return the basket of products
     */
    public Basket getBasket() {
        return basket;
    }

    /**
     * Check if the product is in Stock
     * @param productNum The product number
     */
    public void doCheck(String productNum) {
        basket.clear(); // Clear s. list
        String theAction = "";
        String pn = productNum.trim(); // Product no.
        final int amount = 1; // & quantity
        try {
            if (stockReader.exists(pn)) { // Stock Exists?
                Product pr = stockReader.getDetails(pn); // Product
                if (pr.getQuantity() >= amount) { // In stock?
                    // Display
                    theAction = String.format(
                            "%s : %7.2f (%2d) ",
                            pr.getDescription(),
                            pr.getPrice(),
                            pr.getQuantity()
                    );
                    pr.setQuantity(amount); // Require 1
                    basket.add(pr); // Add to basket
                    picture = stockReader.getImage(pn); // product
                } else {
                    // Inform product not in stock
                    theAction = pr.getDescription() + " not in stock";
                }
            } else {
                // Inform unknown product number.
                theAction = "Unknown product number " + pn;
            }
        } catch (StockException e) {
            DEBUG.error("CustomerClient.doCheck()\n%s", e.getMessage());
        }
        this.pcs.firePropertyChange("action", null, theAction);
    }

    /**
     * Search for a product by its description.
     * @param searchQuery The search query.
     */
    public void search(String searchQuery) {
        basket.clear(); // Clear s. list
        String theAction = "";
        String trimmedQuery = searchQuery.trim();
        final int amount = 1; // & quantity
        try {
            List<Product> products = stockReader.searchByDescription(trimmedQuery);
            Product pr = products.getFirst();

            if (pr.getQuantity() >= amount) { // In stock?
                // Display
                theAction = String.format(
                        "%s : %7.2f (%2d) ",
                        pr.getDescription(),
                        pr.getPrice(),
                        pr.getQuantity()
                );
                pr.setQuantity(amount); // Require 1
                basket.add(pr); // Add to basket
                picture = stockReader.getImage(pr.getProductNum()); // product
            } else {
                // Inform product not in stock
                theAction = pr.getDescription() + " not in stock";
            }
        } catch (StockException e) {
            DEBUG.error("CustomerClient.doCheck()\n%s", e.getMessage());
        }
        this.pcs.firePropertyChange("action", null, theAction);
    }

    /**
     * Clear the products from the basket
     */
    public void doClear() {
        basket.clear(); // Clear s. list
        String theAction = "Enter Product Number"; // Set display
        picture = null; // No picture
        this.pcs.firePropertyChange("action", null, theAction);
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
