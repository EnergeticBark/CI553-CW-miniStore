package clients.packing;

import catalogue.Basket;
import debug.DEBUG;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import middle.MiddleFactory;
import middle.OrderException;
import middle.OrderProcessor;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Implements the Model of the warehouse packing client
 */
public class PackingModel {
    private final AtomicReference<Basket> theBasket = new AtomicReference<>();

    private OrderProcessor theOrder = null;

    final SimpleStringProperty action = new SimpleStringProperty();
    final SimpleStringProperty output = new SimpleStringProperty();

    private final StateOf worker = new StateOf();

    /*
     * Construct the model of the warehouse Packing client
     * @param mf The factory to create the connection objects
     */
    public PackingModel(MiddleFactory mf) {
        try {
            theOrder = mf.makeOrderProcessing();  // Process order
        } catch (Exception e) {
            DEBUG.error("CustomerModel.constructor\n%s", e.getMessage());
        }

        theBasket.set(null); // Initial Basket
        // Start a background check to see when a new order can be packed
        new Thread(this::checkForNewOrder).start();
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

                Basket sb = theOrder.getOrderToPack(); // Order
                if (sb != null) { // Order to pack
                    theBasket.set(sb); // Working on
                    Platform.runLater(() -> fireAction("Bought Receipt"));
                } else {
                    worker.free(); // Free
                    Platform.runLater(() -> fireAction(""));
                }

                Thread.sleep(2000); // idle
            } catch (Exception e) {
                DEBUG.error("%s\n%s", "BackGroundCheck.run()\n%s", e.getMessage());
            }
        }
    }

    /**
     * Return the Basket of products that are to be picked
     * @return the basket
     */
    public Basket getBasket() {
        return theBasket.get();
    }

    /**
     * Process a packed Order
     */
    public void doPacked() {
        try {
            Basket basket = theBasket.get(); // Basket being packed
            if (basket == null) {
                fireAction("No order"); // Not packed order
                return;
            }

            theBasket.set(null); // packed
            int no = basket.getOrderNum(); // Order no
            theOrder.informOrderPacked(no); // Tell system
            worker.free(); // Can pack some more
            fireAction(""); // Inform picker
        } catch (OrderException e) { // Error
            // Of course should not happen
            DEBUG.error("ReceiptModel.doOk()\n%s\n", e.getMessage());
        }
    }
}
