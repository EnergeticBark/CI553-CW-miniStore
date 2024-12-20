package remote;

import middle.OrderException;
import orders.Order;

import java.rmi.RemoteException;

/**
 * The order processing handling.
 * This code is incomplete
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public class RemoteOrder extends Order implements RemoteOrderProcessor {
    public RemoteOrder() throws RemoteException, OrderException {
        super();
    }
}
