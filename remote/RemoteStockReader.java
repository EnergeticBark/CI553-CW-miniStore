package remote;

import catalogue.Product;
import middle.StockException;

import javax.swing.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Defines the RMI interface for read access to the stock object.
 * Like StockReader, but each method could throw a RemoteException.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public interface RemoteStockReader extends Remote {
    boolean exists(String number) throws RemoteException, StockException;
    Product getDetails(String number) throws RemoteException, StockException;
    ImageIcon getImage(String number) throws RemoteException, StockException;
}
