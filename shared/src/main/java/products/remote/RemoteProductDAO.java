package products.remote;

import dao.DAOException;
import dao.RemoteDAO;
import products.Product;

import java.rmi.RemoteException;
import java.util.List;

public interface RemoteProductDAO extends RemoteDAO<Product> {
    List<Product> search(String searchQuery) throws RemoteException, DAOException;
}
