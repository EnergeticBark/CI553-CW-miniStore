/**
 * @author  Mike Smith University of Brighton
 * Interface Middle factory
 * @version 2.0
 */

package middle;

import orders.OrderProcessor;
import orders.exceptions.OrderException;
import products.ProductDAO;

/**
 * Provide access to middle tier components.
 */

// Pattern: Abstract Factory
public interface MiddleFactory {
    /**
     * Return an object to access the database.
     * @return instance of StockDAO
     * @throws DAOException if issue
     */
    ProductDAO makeStockDAO() throws DAOException;

    /**
     * Return an object to access the order processing system
     * @return instance of OrderProcessor
     * @throws OrderException if issue
     */
    OrderProcessor makeOrderProcessing() throws OrderException, DAOException;
}

