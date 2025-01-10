package orders.remote;

import dao.DAOException;
import dao.RemoteDAO;
import orders.Order;

import java.rmi.RemoteException;

public interface RemoteOrderDAO extends RemoteDAO<Order> {
    int getNextOrderNumber() throws DAOException, RemoteException;
}
