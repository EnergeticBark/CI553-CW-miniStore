package orders.remote;

import dao.RemoteDAOWrapper;
import dao.DAOException;
import orders.Order;
import orders.OrderDAO;

import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * Wrapper for allowing the {@link RemoteOrderDAO} interface to be used as a local {@link OrderDAO}.
 * This class catches {@link RemoteException}s and rethrows them as {@link DAOException}s.
 * All CRUD operations are performed in the class that implements RemoteOrderDAO.
 */
public class RemoteOrderDAOWrapper extends RemoteDAOWrapper<Order, RemoteOrderDAO> implements OrderDAO {
    public RemoteOrderDAOWrapper(String url) {
        super(url);
    }

    @Override
    protected void connect() throws DAOException {
        // Setup connection
        try {
            stub = (RemoteOrderDAO) Naming.lookup(url); // Stub returned
        } catch (Exception e) {
            // Failure to attach to the object.
            stub = null;
            throw new DAOException("Com: " + e.getMessage());
        }
    }

    @Override
    public int getNextOrderNumber() throws DAOException {
        return wrapRemote(() -> stub.getNextOrderNumber());
    }
}
