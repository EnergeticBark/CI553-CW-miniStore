package middle;

import catalogue.Product;
import debug.DEBUG;
import remote.RemoteStockReader;

import javax.swing.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Facade for read access to the stock list.
 * The actual implementation of this is held on the middle tier.
 * The actual stock list is held in a relational DataBase on the
 * third tier.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public class StockReaderProvider implements StockReader {
    private RemoteStockReader stub = null;
    private final String url;

    public StockReaderProvider(String url) {
        DEBUG.trace("StockReaderProvider: %s", url);
        this.url = url;
    }

    private void connect() throws StockException {
        // Setup connection
        try {
            stub = (RemoteStockReader) Naming.lookup(url); // Stub returned
        } catch (Exception e) {
            // Failure to attach to the object.
            stub = null;
            throw new StockException("Com: " + e.getMessage());
        }
    }

    /**
     * Checks if the product exits in the stock list
     * @return true if exists otherwise false
     */
    public synchronized boolean exists(String number) throws StockException {
        DEBUG.trace("StockReaderProvider:exists()");
        try {
            if (stub == null) {
                connect();
            }
            return stub.exists(number);
        } catch (RemoteException e) {
            stub = null;
            throw new StockException("Net: " + e.getMessage());
        }
    }

    public synchronized List<Product> searchByDescription(String searchQuery) throws StockException {
        DEBUG.trace("StockReaderProvider:searchByDescription()");
        try {
            if (stub == null) {
                connect();
            }
            return stub.searchByDescription(searchQuery);
        } catch (RemoteException e) {
            stub = null;
            throw new StockException("Net: " + e.getMessage());
        }
    }

    /**
     * Returns details about the product in the stock list
     * @return StockNumber, Description, Price, Quantity
     */
    public synchronized Product getDetails(String number) throws StockException {
        DEBUG.trace("StockReaderProvider:getDetails()");
        try {
            if (stub == null) {
                connect();
            }
            return stub.getDetails(number);
        } catch (RemoteException e) {
            stub = null;
            throw new StockException("Net: " + e.getMessage());
        }
    }

    public synchronized ImageIcon getImage(String number) throws StockException {
        DEBUG.trace("StockReaderProvider:getImage()");
        try {
            if (stub == null) {
                connect();
            }
            return stub.getImage(number);
        } catch (RemoteException e) {
            stub = null;
            throw new StockException("Net: " + e.getMessage());
        }
    }
}
