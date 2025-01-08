package orders.remote;

import dao.RemoteDAOWrapper;
import dao.DAOException;
import orders.Order;
import orders.OrderDAO;

import java.rmi.Naming;
import java.util.List;

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
    public List<Order> getAll() throws DAOException {
        return wrapRemote(() -> stub.getAll());
    }
}
