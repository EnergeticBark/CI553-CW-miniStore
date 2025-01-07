package orders.remote;

import middle.DAOException;
import middle.RemoteDAO;
import orders.Order;

import java.rmi.RemoteException;
import java.util.List;

public interface RemoteOrderDAO extends RemoteDAO<Order> {
    List<Order> getAll() throws DAOException, RemoteException;
}
