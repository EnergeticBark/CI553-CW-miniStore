import catalogue.Basket;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import middle.MiddleFactory;
import orders.Order;
import orders.exceptions.OrderException;
import orders.OrderProcessor;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Implements the Model of the warehouse packing client
 */
public class PackingModel {
    private final AtomicReference<Order> order = new AtomicReference<>();

    private OrderProcessor orderProcessor = null;

    final SimpleStringProperty action = new SimpleStringProperty();
    final SimpleStringProperty output = new SimpleStringProperty();

    private final StateOf worker = new StateOf();

    private static final System.Logger LOGGER = System.getLogger(PackingModel.class.getName());

    /*
     * Construct the model of the warehouse Packing client
     * @param mf The factory to create the connection objects
     */
    public PackingModel(MiddleFactory mf) {
        try {
            orderProcessor = mf.makeOrderProcessing();  // Process order
        } catch (Exception e) {
            LOGGER.log(System.Logger.Level.ERROR, e);
            System.exit(-1);
        }

        order.set(null); // Initial Order

        // Start a background check to see when a new order can be packed
        Thread pollOrders = new Thread(this::checkForNewOrder);
        // Let the JVM exit without waiting for this thread to terminate.
        pollOrders.setDaemon(true);
        pollOrders.start();
    }

    // Tell the PackingView that the model has changed, so it needs to redraw.
    private void fireAction(String actionMessage) {
        this.action.setValue(actionMessage);
        if (getBasket() == null) {
            output.setValue("");
        } else {
            output.setValue(getBasket().getDetails());
        }
    }


    /**
     * Semaphore used to only allow 1 order
     * to be packed at once by this person
     */
    static class StateOf {
        private boolean held = false;

        /**
         * Claim exclusive access
         * @return true if claimed else false
         */
        public synchronized boolean claim() { // Semaphore
            return held ? false : (held = true);
        }

        /**
         * Free the lock
         */
        public synchronized void free() {   //
            assert held;
            held = false;
        }
    }

    /**
     * Method run in a separate thread to check if there
     * is a new order waiting to be packed, and we have
     * nothing to do.
     */
    private void checkForNewOrder() {
        while (true) {
            try {
                boolean isFree = worker.claim(); // Are we free
                if (!isFree) {
                    Thread.sleep(2000);
                    continue;
                }

                Order nextOrder = orderProcessor.getOrderToPack(); // Order
                if (nextOrder != null) { // Order to pack
                    order.set(nextOrder); // Working on
                    Platform.runLater(() -> fireAction("Bought Receipt"));
                } else {
                    worker.free(); // Free
                    Platform.runLater(() -> fireAction(""));
                }

                Thread.sleep(2000); // idle
            } catch (Exception e) {
                LOGGER.log(System.Logger.Level.ERROR, e);
                System.exit(-1);
            }
        }
    }

    /**
     * Return the Basket of products that are to be picked
     * @return the basket
     */
    public Basket getBasket() {
        if (order.get() == null) {
            return null;
        }
        return order.get().getBasket();
    }

    /**
     * Process a packed Order
     */
    public void doPacked() {
        try {
            Order packedOrder = this.order.get(); // Order being packed
            if (packedOrder == null) {
                fireAction("No order"); // Not packed order
                return;
            }

            order.set(null); // packed
            int orderNumber = packedOrder.getOrderNumber();
            orderProcessor.informOrderPacked(orderNumber); // Tell system
            worker.free(); // Can pack some more
            fireAction(""); // Inform picker
        } catch (OrderException e) { // Error
            // Of course should not happen
            LOGGER.log(System.Logger.Level.ERROR, e);
            System.exit(-1);
        }
    }
}
