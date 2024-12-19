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
    private static DBStockReader aDBStockReader = null;
    private static DBStockReadWriter aStockRW = null;
    private static Order aOrder = null;

    /**
     * Return an object to access the database for read only access.
     * All users share this same object.
     */
    public StockReader makeStockReader() throws StockException {
        if (aDBStockReader == null) {
            aDBStockReader = new DBStockReader();
        }
        return aDBStockReader;
    }

    /**
     * Return an object to access the database for read/write access.
     * All users share this same object.
     */
    public StockReadWriter makeStockReadWriter() throws StockException {
        if (aStockRW == null) {
            aStockRW = new DBStockReadWriter();
        }
        return aStockRW;
    }

    /**
     * Return an object to access the order processing system.
     * All users share this same object.
     */
    public OrderProcessor makeOrderProcessing() throws OrderException {
        if (aOrder == null) {
            aOrder = new Order();
        }
        return aOrder;
    }
}

