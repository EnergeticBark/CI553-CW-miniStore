package orders.remote;

import dao.DAOException;
import dao.RemoteDAO;
import orders.Order;

import java.rmi.RemoteException;
import java.util.List;

public interface RemoteOrderDAO extends RemoteDAO<Order> {
    List<Order> getAll() throws DAOException, RemoteException;
}
