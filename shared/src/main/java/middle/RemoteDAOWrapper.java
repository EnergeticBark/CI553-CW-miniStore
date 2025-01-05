package middle;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Facade for read access to the stock list.
 * The actual implementation of this is held on the middle tier.
 * The actual stock list is held in a relational DataBase on the
 * third tier.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public abstract class RemoteDAOWrapper<T> implements DAO<T> {
    protected String url;
    protected RemoteDAO<T> stub = null;

    protected RemoteDAOWrapper(String url) {
        this.url = url;
    }

    protected abstract void connect() throws DAOException;

    protected <R> R wrapRemote(RemoteMethod<R, DAOException> remoteMethod) throws DAOException {
        try {
            if (stub == null) {
                connect();
            }
            return remoteMethod.apply();
        } catch (RemoteException e) {
            stub = null;
            throw new DAOException("Net: " + e.getMessage());
        }
    }

    /**
     * Checks if the product exits in the stock list
     * @return true if exists otherwise false
     */
    @Override
    public synchronized boolean exists(String number) throws DAOException {
        return wrapRemote(() -> stub.exists(number));
    }

    @Override
    public synchronized List<T> search(String searchQuery) throws DAOException {
        return wrapRemote(() -> stub.search(searchQuery));
    }

    /**
     * Returns details about the product in the stock list
     * @return StockNumber, Description, Price, Quantity
     */
    @Override
    public synchronized T get(String number) throws DAOException {
        return wrapRemote(() -> stub.get(number));
    }

    /**
     * Modifies Stock details for a given product number.
     * Information modified: Description, Price
     * @param detail Replace with this version of product
     * @throws DAOException if issue
     */
    @Override
    public synchronized void update(T detail) throws DAOException {
        this.<Void>wrapRemote(() -> {
            stub.update(detail);
            return null;
        });
    }
}

