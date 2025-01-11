/**
 * @author  Mike Smith University of Brighton
 * @version 2.1
 */

package middle;

import dao.DAOException;
import orders.OrderDAO;
import orders.dataaccess.SQLOrderDAO;
import products.dataaccess.SQLProductDAO;
import products.ProductDAO;

/**
 * Provide access to middle tier components.
 * Now only one instance of each middle tier object is created
 */

// Pattern: Abstract Factory
public class LocalMiddleFactory implements MiddleFactory {
    private static SQLProductDAO stockDAO = null;
    private static SQLOrderDAO orderDAO = null;

    /**
     * Return an object to access the database.
     * All users share this same object.
     */
    @Override
    public ProductDAO makeStockDAO() throws DAOException {
        if (stockDAO == null) {
            stockDAO = new SQLProductDAO();
        }
        return stockDAO;
    }

    /**
     * Return an object to access the order processing system.
     * All users share this same object.
     */
    @Override
    public OrderDAO makeOrderDAO() throws DAOException {
        if (orderDAO == null) {
            orderDAO = new SQLOrderDAO();
        }
        return orderDAO;
    }
}

