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

/**
 * Implements the Model of the customer client
 */
public class CustomerModel {
    private final SwingPropertyChangeSupport pcs = new SwingPropertyChangeSupport(this);

    private Basket theBasket = null; // Bought items

    private StockReader theStock = null;
    private ImageIcon thePic = null;

    /*
     * Construct the model of the Customer
     * @param mf The factory to create the connection objects
     */
    public CustomerModel(MiddleFactory mf) {
        try {
            theStock = mf.makeStockReader(); // Database access
        } catch (Exception e) {
            DEBUG.error("""
                    CustomerModel.constructor
                    Database not created?
                    %s
                    """, e.getMessage());
        }
        theBasket = makeBasket(); // Initial Basket
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    /**
     * @return the basket of products
     */
    public Basket getBasket() {
        return theBasket;
    }

    /**
     * Check if the product is in Stock
     * @param productNum The product number
     */
    public void doCheck(String productNum) {
        theBasket.clear(); // Clear s. list
        String theAction = "";
        String pn = productNum.trim(); // Product no.
        final int amount = 1; // & quantity
        try {
            if (theStock.exists(pn)) { // Stock Exists?
                Product pr = theStock.getDetails(pn); // Product
                if (pr.getQuantity() >= amount) { // In stock?
                    // Display
                    theAction = String.format(
                            "%s : %7.2f (%2d) ",
                            pr.getDescription(),
                            pr.getPrice(),
                            pr.getQuantity()
                    );
                    pr.setQuantity(amount); // Require 1
                    theBasket.add(pr); // Add to basket
                    thePic = theStock.getImage(pn); // product
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
     * Clear the products from the basket
     */
    public void doClear() {
        theBasket.clear(); // Clear s. list
        String theAction = "Enter Product Number"; // Set display
        thePic = null; // No picture
        this.pcs.firePropertyChange("action", null, theAction);
    }

    /**
     * Return a picture of the product
     * @return An instance of an ImageIcon
     */
    public ImageIcon getPicture() {
        return thePic;
    }

    /**
     * Make a new Basket
     * @return an instance of a new Basket
     */
    protected Basket makeBasket() {
        return new BetterBasket();
    }
}
