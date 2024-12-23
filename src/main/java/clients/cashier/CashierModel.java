package clients.cashier;

import catalogue.Basket;
import catalogue.BetterBasket;
import catalogue.Product;
import debug.DEBUG;
import javafx.beans.property.SimpleStringProperty;
import middle.*;

/**
 * Implements the Model of the cashier client
 */
public class CashierModel {
    private enum State { process, checked }

    private State theState; // Current state
    private Product theProduct = null; // Current product
    private Basket theBasket = null; // Bought items

    private StockReadWriter theStock = null;
    private OrderProcessor theOrder = null;

    final SimpleStringProperty action = new SimpleStringProperty();
    final SimpleStringProperty output = new SimpleStringProperty();

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

    // Tell the CustomerView that the model has changed, so it needs to redraw.
    private void fireAction(String actionMessage) {
        this.action.setValue(actionMessage);
        if (getBasket() == null) {
            output.setValue("Customers order");
        } else {
            output.setValue(getBasket().getDetails());
        }
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
     * @param productNumber The product number
     */
    public void checkStock(String productNumber) {
        theState = State.process; // State process
        final String trimmedProductNumber = productNumber.trim(); // Product no.
        try {
            if (!theStock.exists(trimmedProductNumber)) { // Product doesn't exist?
                fireAction("Unknown product number " + trimmedProductNumber);
                return;
            }

            Product product = theStock.getDetails(trimmedProductNumber);
            if (product.getQuantity() < 1) { // Out of stock?
                fireAction(product.getDescription() + " not in stock");
                return;
            }

            theProduct = product; // Remember prod.
            theProduct.setQuantity(1); // & quantity
            theState = State.checked; // OK await BUY
            // Display product in action label.
            fireAction(String.format("%s : %7.2f (%2d) ",
                    product.getDescription(),
                    product.getPrice(),
                    product.getQuantity()
            ));
        } catch (StockException e) {
            DEBUG.error("%s\n%s", "CashierModel.doCheck", e.getMessage());
            fireAction(e.getMessage());
        }
    }

    /**
     * Buy the product
     */
    public void buy() {
        try {
            if (theState != State.checked) { // Not checked
                // with customer
                fireAction("please check its availability");
                return;
            }

            boolean stockBought = theStock.buyStock(
                    theProduct.getProductNum(),
                    theProduct.getQuantity()
            );
            // Buy however may fail
            if (!stockBought) { // Stock not bought
                fireAction("!!! Not in stock"); // Now no stock
                return;
            }

            makeBasketIfReq(); // new Basket ?
            theBasket.add(theProduct); // Add to bought
            fireAction("Purchased " + theProduct.getDescription());
        } catch (StockException e) {
            DEBUG.error("%s\n%s", "CashierModel.doBuy", e.getMessage());
            fireAction(e.getMessage());
        }
        theState = State.process; // All Done
    }

    /**
     * Customer pays for the contents of the basket
     */
    public void bought() {
        try {
            if (theBasket != null && !theBasket.isEmpty()) { // items > 1
                // T
                theOrder.newOrder(theBasket); // Process order
            }
            theBasket = null;
            theState = State.process; // All Done
            fireAction("Start New Order");
        } catch (OrderException e) {
            DEBUG.error("%s\n%s", "CashierModel.doCancel", e.getMessage());
            fireAction(e.getMessage());
        }
    }

    /**
     * ask for update of view called at start of day
     * or after system reset
     */
    public void askForUpdate() {
        fireAction("Welcome");
    }

    /**
     * make a Basket when required
     */
    private void makeBasketIfReq() {
        if (theBasket == null) {
            try {
                int uon = theOrder.uniqueNumber(); // Unique order num.
                theBasket = makeBasket(); // basket list
                theBasket.setOrderNum(uon); // Add an order number
            } catch (OrderException e) {
                DEBUG.error("""
                        Communications failure
                        CashierModel.makeBasket()
                        %s
                        """, e.getMessage());
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
  
