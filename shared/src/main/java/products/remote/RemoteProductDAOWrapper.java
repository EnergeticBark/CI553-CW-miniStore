package products.remote;

import dao.RemoteDAOWrapper;
import dao.DAOException;
import products.Product;
import products.ProductDAO;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Wrapper for allowing the {@link RemoteProductDAO} interface to be used as a local {@link ProductDAO}.
 * This class catches {@link RemoteException}s and rethrows them as {@link DAOException}s.
 * All CRUD operations are performed in the class that implements RemoteProductDAO.
 */
public class RemoteProductDAOWrapper extends RemoteDAOWrapper<Product, RemoteProductDAO> implements ProductDAO {
    public RemoteProductDAOWrapper(String url) {
        super(url);
    }

    @Override
    protected void connect() throws DAOException {
        // Setup connection
        try {
            stub = (RemoteProductDAO) Naming.lookup(url); // Stub returned
        } catch (Exception e) {
            // Failure to attach to the object.
            stub = null;
            throw new DAOException("Com: " + e.getMessage());
        }
    }

    @Override
    public synchronized List<Product> search(String searchQuery) throws DAOException {
        return wrapRemote(() -> stub.search(searchQuery));
    }
}
