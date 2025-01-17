package middle;

import dao.DAOException;
import orders.OrderDAO;
import orders.dataaccess.SQLOrderDAO;
import products.dataaccess.SQLProductDAO;
import products.ProductDAO;

// Pattern: Abstract Factory

/** Provide access to middle tier components. Now only one instance of each middle tier object is created */
public class LocalMiddleFactory implements MiddleFactory {
    private static SQLProductDAO stockDAO = null;
    private static SQLOrderDAO orderDAO = null;

    /** {@return an object to access the database. All users share this same object} */
    @Override
    public ProductDAO makeStockDAO() throws DAOException {
        if (stockDAO == null) {
            stockDAO = new SQLProductDAO();
        }
        return stockDAO;
    }

    /** {@return an object to access the order processing system. All users share this same object} */
    @Override
    public OrderDAO makeOrderDAO() throws DAOException {
        if (orderDAO == null) {
            orderDAO = new SQLOrderDAO();
        }
        return orderDAO;
    }
}

