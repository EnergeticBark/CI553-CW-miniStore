package orders.remote;

import catalogue.Basket;
import debug.DEBUG;
import middle.AbstractRemoteWrapper;
import orders.OrderProcessor;
import orders.exceptions.OrderException;

import java.rmi.Naming;
import java.rmi.RemoteException;
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
public class RemoteOrderProcessorWrapper extends AbstractRemoteWrapper implements OrderProcessor {
    private RemoteOrderProcessor stub = null;

    public RemoteOrderProcessorWrapper(String url) {
        super(url);
    }

    @Override
    protected void connect() throws OrderException {
        // Setup connection
        try {
            stub = (RemoteOrderProcessor) Naming.lookup(url);
        } catch (Exception e) {
            // Failure to attach to the object.
            stub = null;
            throw new OrderException("Com: " + e.getMessage());  // object
        }
    }

    private <R> R wrapRemote(RemoteMethod<R, OrderException> remoteMethod) throws OrderException {
        try {
            if (stub == null) {
                connect();
            }
            return remoteMethod.apply();
        } catch (RemoteException e) {
            stub = null;
            throw new OrderException("Net: " + e.getMessage());
        }
    }

    @Override
    public void newOrder(Basket bought) throws OrderException {
        DEBUG.trace("RemoteOrderProcessorWrapper:newOrder()");
        wrapRemote(() -> {
            stub.newOrder(bought);
            return null;
        });
    }

    @Override
    public int uniqueNumber() throws OrderException {
        DEBUG.trace("RemoteOrderProcessorWrapper:uniqueNumber()");
        return wrapRemote(() -> stub.uniqueNumber());
    }

    /**
     * Returns an order to pick from the warehouse
     * if no order then returns null.
     * @return An order to pick
     */
    @Override
    public synchronized Basket getOrderToPack() throws OrderException {
        DEBUG.trace("RemoteOrderProcessorWrapper:getOrderTioPack()");
        return wrapRemote(() -> stub.getOrderToPack());
    }

    /**
     * Informs the order processing system that the order has been
     * picked and the products are now on the conveyor belt to
     * the shop floor.
     */
    @Override
    public synchronized boolean informOrderPacked(int orderNum) throws OrderException {
        DEBUG.trace("RemoteOrderProcessorWrapper:informOrderPacked()");
        return wrapRemote(() -> stub.informOrderPacked(orderNum));
    }

    /**
     * Informs the order processing system that the order has been
     * collected by the customer
     */
    @Override
    public synchronized boolean informOrderCollected(int orderNum) throws OrderException {
        DEBUG.trace("RemoteOrderProcessorWrapper:informOrderCollected()" );
        return wrapRemote(() -> stub.informOrderCollected(orderNum));
    }

    /**
     * Returns information about all orders in the order processing system
     */
    @Override
    public synchronized Map<String, List<Integer>> getOrderState() throws OrderException {
        DEBUG.trace("RemoteOrderProcessorWrapper:getOrderState()");
        return wrapRemote(() -> stub.getOrderState());
    }
}
