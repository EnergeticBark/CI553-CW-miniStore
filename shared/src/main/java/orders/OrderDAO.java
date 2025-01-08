package orders;

import dao.DAO;
import dao.DAOException;

import java.util.List;

public interface OrderDAO extends DAO<Order> {
    List<Order> getAll() throws DAOException;
    int getNextOrderNumber() throws DAOException;
}
