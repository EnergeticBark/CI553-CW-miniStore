package orders;

import dao.DAO;
import dao.DAOException;

/** A {@link DAO} to perform CRUD operations on {@link Order}, with additional methods specific to Order. */
public interface OrderDAO extends DAO<Order> {
    /** {@return a unique order number} */
    int getNextOrderNumber() throws DAOException;
}
