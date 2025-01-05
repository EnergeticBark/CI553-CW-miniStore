package middle;

import catalogue.Product;

import java.rmi.Naming;

public class RemoteProductDAOWrapper extends AbstractRemoteDAOWrapper<Product> implements ProductDAO {
    public RemoteProductDAOWrapper(String url) {
        super(url);
    }

    @Override
    void connect() throws DAOException {
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
