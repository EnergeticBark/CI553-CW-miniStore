package remote;

import catalogue.Product;
import middle.StockException;

import java.rmi.RemoteException;

/**
 * Defines the RMI interface for read/write access to the stock object.
 * Like StockReadWriter, but each method could throw a RemoteException.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public interface RemoteStockReadWriter extends RemoteStockReader {
    boolean buyStock(String number, int amount) throws RemoteException, StockException;
    void addStock(String number, int amount) throws RemoteException, StockException;
    void modifyStock(Product detail) throws RemoteException, StockException;
}
