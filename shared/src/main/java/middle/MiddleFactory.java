/**
 * @author  Mike Smith University of Brighton
 * Interface Middle factory
 * @version 2.0
 */

package middle;

import catalogue.Product;

/**
 * Provide access to middle tier components.
 */

// Pattern: Abstract Factory
public interface MiddleFactory {
    /**
     * Return an object to access the database.
     * @return instance of StockDAO
     * @throws StockException if issue
     */
    DAO<Product> makeStockDAO() throws StockException;

    /**
     * Return an object to access the order processing system
     * @return instance of OrderProcessor
     * @throws OrderException if issue
     */
    OrderProcessor makeOrderProcessing() throws OrderException;
}

