package remote;

import catalogue.Basket;
import middle.OrderException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 * Defines the RMI interface for the OrderProcessor.
 * Like OrderProcessor, but each method could throw a RemoteException.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public interface RemoteOrderProcessor extends Remote {
    void newOrder(Basket order) throws RemoteException, OrderException;
    int uniqueNumber() throws RemoteException, OrderException;
    Basket getOrderToPack() throws RemoteException, OrderException;
    boolean informOrderPacked(int orderNum) throws RemoteException, OrderException;
    boolean informOrderCollected(int orderNum) throws RemoteException, OrderException;
    Map<String, List<Integer>> getOrderState() throws RemoteException, OrderException;
}
