package orders.remote;

import dao.DAOException;
import dao.RemoteDAO;
import orders.Order;

import java.rmi.RemoteException;

/**
 * Defines the RMI interface for remote access to an OrderDAO.
 * The same as {@link orders.OrderDAO}, but each method can throw a RemoteException.
 */
public interface RemoteOrderDAO extends RemoteDAO<Order> {
    int getNextOrderNumber() throws DAOException, RemoteException;
}
