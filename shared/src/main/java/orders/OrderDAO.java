package orders;

import dao.DAO;
import dao.DAOException;

public interface OrderDAO extends DAO<Order> {
    int getNextOrderNumber() throws DAOException;
}
