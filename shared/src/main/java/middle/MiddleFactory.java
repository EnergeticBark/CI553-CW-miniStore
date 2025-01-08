/**
 * @author  Mike Smith University of Brighton
 * Interface Middle factory
 * @version 2.0
 */

package middle;

import orders.OrderDAO;
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
     * @return instance of OrderDAO
     * @throws DAOException if issue
     */
    OrderDAO makeOrderDAO() throws DAOException;
}

