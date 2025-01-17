package products.remote;

import dao.DAOException;
import dao.RemoteDAO;
import products.Product;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Defines the RMI interface for remote access to a ProductDAO.
 * The same as {@link products.ProductDAO}, but each method can throw a RemoteException.
 */
public interface RemoteProductDAO extends RemoteDAO<Product> {
    List<Product> search(String searchQuery) throws RemoteException, DAOException;
}
