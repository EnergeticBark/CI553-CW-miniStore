package middle;

import debug.DEBUG;
import remote.RemoteDAO;

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
public class RemoteDAOWrapper<T> implements DAO<T> {
    private RemoteDAO<T> stub = null;
    private final String url;

    public RemoteDAOWrapper(String url) {
        DEBUG.trace("RemoteDAOWrapper: %s", url);
        this.url = url;
    }

    @SuppressWarnings("unchecked")
    private void connect() throws StockException {
        // Setup connection
        try {
            stub = (RemoteDAO<T>) Naming.lookup(url); // Stub returned
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
    @Override
    public synchronized boolean exists(String number) throws StockException {
        DEBUG.trace("RemoteDAOWrapper:exists()");
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

    @Override
    public synchronized List<T> search(String searchQuery) throws StockException {
        DEBUG.trace("RemoteDAOWrapper:search()");
        try {
            if (stub == null) {
                connect();
            }
            return stub.search(searchQuery);
        } catch (RemoteException e) {
            stub = null;
            throw new StockException("Net: " + e.getMessage());
        }
    }

    /**
     * Returns details about the product in the stock list
     * @return StockNumber, Description, Price, Quantity
     */
    @Override
    public synchronized T get(String number) throws StockException {
        DEBUG.trace("RemoteDAOWrapper:get()");
        try {
            if (stub == null) {
                connect();
            }
            return stub.get(number);
        } catch (RemoteException e) {
            stub = null;
            throw new StockException("Net: " + e.getMessage());
        }
    }

    /**
     * Modifies Stock details for a given product number.
     * Information modified: Description, Price
     * @param detail Replace with this version of product
     * @throws middle.StockException if issue
     */
    @Override
    public synchronized void update(T detail) throws StockException {
        DEBUG.trace("RemoteDAOWrapper:update()");
        try {
            if (stub == null) {
                connect();
            }
            stub.update(detail);
        } catch (RemoteException e) {
            stub = null;
            throw new StockException("Net: " + e.getMessage());
        }
    }
}
