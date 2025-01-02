package remote;

import middle.StockException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Defines the RMI interface for read access to the stock object.
 * Like StockDAO, but each method could throw a RemoteException.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public interface RemoteDAO<T> extends Remote {
    boolean exists(String number) throws RemoteException, StockException;
    List<T> search(String searchQuery) throws RemoteException, StockException;
    T get(String number) throws RemoteException, StockException;
    void update(T detail) throws RemoteException, StockException;
}
