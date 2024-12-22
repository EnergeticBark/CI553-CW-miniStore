package clients.cashier;

import catalogue.Basket;
import catalogue.BetterBasket;
import catalogue.Product;
import debug.DEBUG;
import middle.*;

import javax.swing.event.SwingPropertyChangeSupport;
import java.beans.PropertyChangeListener;

/**
 * Implements the Model of the cashier client
 */
public class CashierModel {
    private final SwingPropertyChangeSupport pcs = new SwingPropertyChangeSupport(this);

    private enum State { process, checked }

    private State theState = State.process; // Current state
    private Product theProduct = null; // Current product
    private Basket theBasket = null; // Bought items

    private String pn = ""; // Product being processed

    private StockReadWriter theStock = null;
    private OrderProcessor theOrder = null;

    /**
     * Construct the model of the Cashier
     * @param mf The factory to create the connection objects
     */
    public CashierModel(MiddleFactory mf) {
        try {
            theStock = mf.makeStockReadWriter(); // Database access
            theOrder = mf.makeOrderProcessing(); // Process order
        } catch (Exception e) {
            DEBUG.error("CashierModel.constructor\n%s", e.getMessage());
        }
        theState = State.process; // Current state
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    /**
     * Get the Basket of products
     * @return basket
     */
    public Basket getBasket() {
        return theBasket;
    }

    /**
     * Check if the product is in Stock
     * @param productNum The product number
     */
    public void doCheck(String productNum) {
        String theAction = "";
        theState = State.process; // State process
        pn = productNum.trim(); // Product no.
        int amount = 1; // & quantity
        try {
            if (theStock.exists(pn)) { // Stock Exists?
                // T
                Product pr = theStock.getDetails(pn); // Get details
                if (pr.getQuantity() >= amount) { // In stock?
                    // T
                    theAction = // Display
                            String.format("%s : %7.2f (%2d) ",
                                    pr.getDescription(), // description
                                    pr.getPrice(), // price
                                    pr.getQuantity() // quantity
                            );
                    theProduct = pr; // Remember prod.
                    theProduct.setQuantity(amount); // & quantity
                    theState = State.checked; // OK await BUY
                } else {
                    // F
                    theAction = // Not in Stock
                            pr.getDescription() + " not in stock";
                }
            } else {
                // F Stock exists
                theAction = // Unknown
                        "Unknown product number " + pn; // product no.
            }
        } catch (StockException e) {
            DEBUG.error("%s\n%s", "CashierModel.doCheck", e.getMessage());
            theAction = e.getMessage();
        }
        this.pcs.firePropertyChange("action", null, theAction);
    }

    /**
     * Buy the product
     */
    public void doBuy() {
        String theAction = "";
        int amount  = 1; // & quantity
        try {
            if (theState != State.checked) { // Not checked
                // with customer
                theAction = "please check its availability";
            } else {
                boolean stockBought = // Buy
                        theStock.buyStock( // however
                                theProduct.getProductNum(), // may fail
                                theProduct.getQuantity()
                        );
                if (stockBought) { // Stock bought
                    // T
                    makeBasketIfReq(); // new Basket ?
                    theBasket.add(theProduct); // Add to bought
                    theAction = "Purchased " + // details
                            theProduct.getDescription();
                } else {
                    // F
                    theAction = "!!! Not in stock"; // Now no stock
                }
            }
        } catch (StockException e) {
            DEBUG.error("%s\n%s", "CashierModel.doBuy", e.getMessage());
            theAction = e.getMessage();
        }
        theState = State.process; // All Done
        this.pcs.firePropertyChange("action", null, theAction);
    }

    /**
     * Customer pays for the contents of the basket
     */
    public void doBought() {
        String theAction = "";
        int amount = 1; // & quantity
        try {
            if (theBasket != null && theBasket.size() >= 1) { // items > 1
                // T
                theOrder.newOrder(theBasket); // Process order
                theBasket = null; // reset
            }
            theAction = "Start New Order"; // New order
            theState = State.process; // All Done
            theBasket = null;
        } catch (OrderException e) {
            DEBUG.error("%s\n%s", "CashierModel.doCancel", e.getMessage());
            theAction = e.getMessage();
        }
        theBasket = null;
        this.pcs.firePropertyChange("action", null, theAction); // Notify
    }

    /**
     * ask for update of view called at start of day
     * or after system reset
     */
    public void askForUpdate() {
        this.pcs.firePropertyChange("action", null, "Welcome");
    }

    /**
     * make a Basket when required
     */
    private void makeBasketIfReq() {
        if (theBasket == null) {
            try {
                int uon = theOrder.uniqueNumber(); // Unique order num.
                theBasket = makeBasket(); // basket list
                theBasket.setOrderNum( uon ); // Add an order number
            } catch (OrderException e) {
                DEBUG.error("Communications failure\n" + "CashierModel.makeBasket()\n%s", e.getMessage());
            }
        }
    }

    /**
     * return an instance of a new Basket
     * @return an instance of a new Basket
     */
    protected Basket makeBasket() {
        return new BetterBasket();
    }
}
  