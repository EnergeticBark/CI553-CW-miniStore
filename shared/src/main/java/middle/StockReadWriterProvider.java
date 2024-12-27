package middle;

import catalogue.Product;
import debug.DEBUG;
import remote.RemoteStockReadWriter;

import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * Facade for read/write access to the stock list.
 * The actual implementation of this is held on the middle tier.
 * The actual stock list is held in a relational DataBase on the
 * third tier.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public class StockReadWriterProvider extends StockReaderProvider implements StockReadWriter {
    private RemoteStockReadWriter stub = null;
    private final String url;

    public StockReadWriterProvider(String url) {
        super(url); // Not used
        this.url = url;
    }

    private void connect() throws StockException {
        // Setup connection
        try {
            stub = (RemoteStockReadWriter) Naming.lookup(url); // Stub returned
        } catch (Exception e) {
            // Failure to attach to the object.
            stub = null;
            throw new StockException("Com: " + e.getMessage());
        }
    }

    /**
     * Buys stock and hence decrements number in stock list
     * @return StockNumber, Description, Price, Quantity
     * @throws StockException if remote exception
     */
    public boolean buyStock(String number, int amount) throws StockException {
        DEBUG.trace("StockReadWriterProvider:buyStock()");
        try {
            if (stub == null) {
                connect();
            }
            return stub.buyStock(number, amount);
        } catch (RemoteException e) {
            stub = null;
            throw new StockException("Net: " + e.getMessage());
        }
    }

    /**
     * Adds (Restocks) stock to the product list
     * @param number Stock number
     * @param amount of stock
     * @throws StockException if remote exception
     */
    public void addStock(String number, int amount) throws StockException {
        DEBUG.trace("StockReadWriterProvider:addStock()");
        try {
            if (stub == null) {
                connect();
            }
            stub.addStock(number, amount);
        } catch (RemoteException e) {
            stub = null;
            throw new StockException("Net: " + e.getMessage());
        }
    }

    /**
     * Modifies Stock details for a given product number.
     * Information modified: Description, Price
     * @param detail Stock details to be modified
     * @throws StockException if remote exception
     */
    public void modifyStock(Product detail) throws StockException {
        DEBUG.trace("StockReadWriterProvider:modifyStock()");
        try {
            if (stub == null) {
                connect();
            }
            stub.modifyStock(detail);
        } catch (RemoteException e) {
            stub = null;
            throw new StockException("Net: " + e.getMessage());
        }
    }
}
