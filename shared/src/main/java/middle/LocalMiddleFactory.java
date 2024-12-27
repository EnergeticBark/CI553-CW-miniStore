/**
 * @author  Mike Smith University of Brighton
 * @version 2.1
 */

package middle;

import dbAccess.DBStockReader;
import dbAccess.DBStockReadWriter;
import orders.Order;

/**
 * Provide access to middle tier components.
 * Now only one instance of each middle tier object is created
 */

// Pattern: Abstract Factory
public class LocalMiddleFactory implements MiddleFactory {
    private static DBStockReader dbStockReader = null;
    private static DBStockReadWriter dbStockReadWriter = null;
    private static Order order = null;

    /**
     * Return an object to access the database for read only access.
     * All users share this same object.
     */
    public StockReader makeStockReader() throws StockException {
        if (dbStockReader == null) {
            dbStockReader = new DBStockReader();
        }
        return dbStockReader;
    }

    /**
     * Return an object to access the database for read/write access.
     * All users share this same object.
     */
    public StockReadWriter makeStockReadWriter() throws StockException {
        if (dbStockReadWriter == null) {
            dbStockReadWriter = new DBStockReadWriter();
        }
        return dbStockReadWriter;
    }

    /**
     * Return an object to access the order processing system.
     * All users share this same object.
     */
    public OrderProcessor makeOrderProcessing() {
        if (order == null) {
            order = new Order();
        }
        return order;
    }
}

