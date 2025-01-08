import catalogue.Basket;
import catalogue.BetterBasket;
import dao.DAOException;
import orders.OrderDAO;
import orders.usecases.CreateOrder;
import products.Product;
import products.exceptions.ProductDoesNotExistException;
import products.exceptions.ProductOutOfStockException;
import javafx.beans.property.SimpleStringProperty;
import middle.*;
import products.ProductDAO;
import products.usecases.BuyStock;
import products.usecases.EnsureEnoughStock;
import products.usecases.GetProductByNumber;

/**
 * Implements the Model of the cashier client
 */
public class CashierModel {
    private enum State { process, checked }

    private State state; // Current state
    private Product product = null; // Current product
    private Basket basket = null; // Bought items

    private ProductDAO stockDAO = null;
    private OrderDAO orderDAO = null;

    final SimpleStringProperty action = new SimpleStringProperty();
    final SimpleStringProperty output = new SimpleStringProperty();

    private static final System.Logger LOGGER = System.getLogger(CashierModel.class.getName());

    /**
     * Construct the model of the Cashier
     * @param mf The factory to create the connection objects
     */
    public CashierModel(MiddleFactory mf) {
        try {
            stockDAO = mf.makeStockDAO(); // Database access
            orderDAO = mf.makeOrderDAO(); // Process order
        } catch (Exception e) {
            LOGGER.log(System.Logger.Level.ERROR, e);
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
        return basket;
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
            int parsedProductNumber = Integer.parseUnsignedInt(trimmedProductNumber);
            product = new GetProductByNumber(stockDAO).run(parsedProductNumber);
            new EnsureEnoughStock().run(product, 1);

            state = State.checked; // OK await BUY
            // Display product in action label.
            fireAction(product.showDetails());
            product.setQuantity(1);
        } catch (ProductOutOfStockException e) {
            fireAction(e.getMessage() + " not in stock");
        } catch (ProductDoesNotExistException | NumberFormatException _) {
            fireAction("Unknown product number " + trimmedProductNumber);
        } catch (DAOException e) {
            LOGGER.log(System.Logger.Level.ERROR, e);
            System.exit(-1);
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
            basket.add(product); // Add to bought
            fireAction("Purchased " + product.getDescription());
        } catch (ProductOutOfStockException _) {
            fireAction("!!! Not in stock");
        } catch (DAOException e) {
            LOGGER.log(System.Logger.Level.ERROR, e);
            System.exit(-1);
        }
        state = State.process; // All Done
    }

    /**
     * Customer pays for the contents of the basket
     */
    public void bought() {
        try {
            if (basket != null && !basket.isEmpty()) { // items > 1
                // T
                new CreateOrder(orderDAO).run(basket); // Process order
            }
            basket = null;
            state = State.process; // All Done
            fireAction("Start New Order");
        } catch (DAOException e) {
            LOGGER.log(System.Logger.Level.ERROR, e);
            System.exit(-1);
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
        if (basket == null) {
            basket = makeBasket(); // basket list
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
  
