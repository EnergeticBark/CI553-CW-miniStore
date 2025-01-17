package dao;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Defines the RMI interface for remote access to a DAO.
 * The same as {@link DAO}, but each method can throw a RemoteException.
 */
public interface RemoteDAO<T> extends Remote {
    boolean exists(int identifier) throws RemoteException, DAOException;
    T get(int identifier) throws RemoteException, DAOException;
    List<T> getAll() throws DAOException, RemoteException;
    void create(T newEntity) throws RemoteException, DAOException;
    void update(T replacement) throws RemoteException, DAOException;
}
