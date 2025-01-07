package orders;

import middle.DAO;
import middle.DAOException;

import java.util.List;

public interface OrderDAO extends DAO<Order> {
    List<Order> getAll() throws DAOException;
}
