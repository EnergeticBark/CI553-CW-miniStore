package middle;

import dao.DAOException;
import orders.OrderDAO;
import products.ProductDAO;

// Pattern: Abstract Factory

/** Provide access to middle tier components. */
public interface MiddleFactory {
    /**
     * Return an object to access the database.
     * @return instance of StockDAO
     * @throws DAOException if issue
     */
    ProductDAO makeStockDAO() throws DAOException;

    /**
     * Return an object to access the order processing system.
     * @return instance of OrderDAO
     * @throws DAOException if issue
     */
    OrderDAO makeOrderDAO() throws DAOException;
}

