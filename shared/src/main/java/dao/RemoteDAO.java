package dao;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Defines the RMI interface for read access to the stock object.
 * Like StockDAO, but each method could throw a RemoteException.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public interface RemoteDAO<T> extends Remote {
    boolean exists(int identifier) throws RemoteException, DAOException;
    List<T> search(String searchQuery) throws RemoteException, DAOException;
    T get(int identifier) throws RemoteException, DAOException;
    void create(T newEntity) throws RemoteException, DAOException;
    void update(T detail) throws RemoteException, DAOException;
}
