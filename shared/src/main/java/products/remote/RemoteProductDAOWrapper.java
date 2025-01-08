package products.remote;

import middle.RemoteDAOWrapper;
import middle.DAOException;
import products.Product;
import products.ProductDAO;

import java.rmi.Naming;

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
}
