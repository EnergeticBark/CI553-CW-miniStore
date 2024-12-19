package middle;

import catalogue.Basket;
import debug.DEBUG;
import remote.RemoteOrderProcessor;

import java.rmi.Naming;
import java.util.List;
import java.util.Map;

// There can only be 1 ResultSet opened per statement
// so no simultaneous use of the statement object
// hence the synchronized methods

/**
 * Facade for the order processing handling which is implemented on the middle tier.
 * This code is incomplete
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public class OrderProcessorProvider implements OrderProcessor {
    private RemoteOrderProcessor stub = null;
    private final String theOrderURL;

    public OrderProcessorProvider(String url) {
        theOrderURL = url;
    }

    private void connect() throws OrderException {
        // Setup connection
        try {
            stub = (RemoteOrderProcessor) Naming.lookup(theOrderURL);
        } catch (Exception e) {
            // Failure to attach to the object.
            stub = null;
            throw new OrderException("Com: " + e.getMessage());  // object
        }
    }


    public void newOrder(Basket bought) throws OrderException {
        DEBUG.trace("OrderProcessorProvider:newOrder()");
        try {
            if (stub == null) {
                connect();
            }
            stub.newOrder(bought);
        } catch (Exception e) {
            stub = null;
            throw new OrderException("Net: " + e.getMessage());
        }
    }

    public int uniqueNumber() throws OrderException {
        DEBUG.trace("OrderProcessorProvider:uniqueNumber()" );
        try {
            if (stub == null) {
                connect();
            }
            return stub.uniqueNumber();
        } catch (Exception e) {
            stub = null;
            throw new OrderException("Net: " + e.getMessage());
        }
    }

    /**
     * Returns an order to pick from the warehouse
     * if no order then returns null.
     * @return An order to pick
     */

    public synchronized Basket getOrderToPack() throws OrderException {
        DEBUG.trace("OrderProcessorProvider:getOrderTioPack()");
        try {
            if (stub == null) {
                connect();
            }
            return stub.getOrderToPack();
        } catch (Exception e) {
            stub = null;
            throw new OrderException("Net: " + e.getMessage());
        }
    }

    /**
     * Informs the order processing system that the order has been
     * picked and the products are now on the conveyor belt to
     * the shop floor.
     */

    public synchronized boolean informOrderPacked(int orderNum) throws OrderException {
        DEBUG.trace("OrderProcessorProvider:informOrderPacked()");
        try {
            if (stub == null) {
                connect();
            }
            return stub.informOrderPacked(orderNum);
        } catch (Exception e) {
            stub = null;
            throw new OrderException("Net: " + e.getMessage());
        }
    }

    /**
     * Informs the order processing system that the order has been
     * collected by the customer
     */

    public synchronized boolean informOrderCollected(int orderNum) throws OrderException {
        DEBUG.trace("OrderProcessorProvider:informOrderCollected()" );
        try {
            if (stub == null) {
                connect();
            }
            return stub.informOrderCollected(orderNum);
        } catch (Exception e) {
            stub = null;
            throw new OrderException("Net: " + e.getMessage());
        }
    }

    /**
     * Returns information about all orders in the order processing system
     */

    public synchronized Map<String, List<Integer>> getOrderState() throws OrderException {
        DEBUG.trace("OrderProcessorProvider:getOrderState()");
        try {
            if (stub == null) {
                connect();
            }
            return stub.getOrderState();
        } catch (Exception e) {
            stub = null;
            throw new OrderException("Net: " + e.getMessage());
        }
    }
}
