package orders.remote;

import catalogue.Basket;
import orders.Order;
import orders.exceptions.OrderException;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Defines the RMI interface for the OrderProcessor.
 * Like OrderProcessor, but each method could throw a RemoteException.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public interface RemoteOrderProcessor extends Remote {
    void newOrder(Basket order) throws RemoteException, OrderException;
    Order getOrderToPack() throws RemoteException, OrderException;
    void informOrderPacked(int orderNum) throws RemoteException, OrderException;
}
