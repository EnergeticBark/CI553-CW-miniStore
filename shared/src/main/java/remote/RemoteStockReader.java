package remote;

import catalogue.Product;
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
public interface RemoteStockReader extends Remote {
    boolean exists(String number) throws RemoteException, StockException;
    List<Product> searchByDescription(String searchQuery) throws RemoteException, StockException;
    Product getDetails(String number) throws RemoteException, StockException;
    void modifyStock(Product detail) throws RemoteException, StockException;
}
