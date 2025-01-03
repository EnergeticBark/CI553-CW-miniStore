import catalogue.Basket;
import catalogue.BetterBasket;
import catalogue.Product;
import debug.DEBUG;
import exceptions.ProductDoesNotExistException;
import exceptions.ProductOutOfStockException;
import javafx.beans.property.SimpleStringProperty;
import middle.*;
import usecases.BuyStock;
import usecases.EnsureEnoughStock;
import usecases.GetProductByNumber;

/**
 * Implements the Model of the cashier client
 */
public class CashierModel {
    private enum State { process, checked }

    private State state; // Current state
    private Product product = null; // Current product
    private Basket theBasket = null; // Bought items

    private DAO<Product> stockDAO = null;
    private OrderProcessor theOrder = null;

    final SimpleStringProperty action = new SimpleStringProperty();
    final SimpleStringProperty output = new SimpleStringProperty();

    /**
     * Construct the model of the Cashier
     * @param mf The factory to create the connection objects
     */
    public CashierModel(MiddleFactory mf) {
        try {
            stockDAO = mf.makeStockDAO(); // Database access
            theOrder = mf.makeOrderProcessing(); // Process order
        } catch (Exception e) {
            DEBUG.error("CashierModel.constructor\n%s", e.getMessage());
        }
        state = State.process; // Current state
    }

    // Tell the CashierView that the model has changed, so it needs to redraw.
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
        state = State.process; // State process
        final String trimmedProductNumber = productNumber.trim(); // Product no.
        try {
            // Remember prod.
            product = new GetProductByNumber(stockDAO).run(trimmedProductNumber);
            new EnsureEnoughStock().run(product, 1);

            state = State.checked; // OK await BUY
            // Display product in action label.
            fireAction(product.showDetails());
            product.setQuantity(1);
        } catch (ProductOutOfStockException e) {
            fireAction(e.getMessage() + " not in stock");
        } catch (ProductDoesNotExistException _) {
            fireAction("Unknown product number " + trimmedProductNumber);
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
            if (state != State.checked) { // Not checked with customer
                fireAction("please check its availability");
                return;
            }

            new BuyStock(stockDAO).run(product.getProductNumber(), 1);

            makeBasketIfReq(); // new Basket ?
            theBasket.add(product); // Add to bought
            fireAction("Purchased " + product.getDescription());
        } catch (ProductOutOfStockException _) {
            fireAction("!!! Not in stock");
        } catch (StockException e) {
            DEBUG.error("%s\n%s", "CashierModel.doBuy", e.getMessage());
            fireAction(e.getMessage());
        }
        state = State.process; // All Done
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
            state = State.process; // All Done
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
  
