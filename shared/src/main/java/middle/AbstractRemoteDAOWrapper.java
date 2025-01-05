package middle;

import debug.DEBUG;

import java.util.List;

/**
 * Facade for read access to the stock list.
 * The actual implementation of this is held on the middle tier.
 * The actual stock list is held in a relational DataBase on the
 * third tier.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public abstract class AbstractRemoteDAOWrapper<T> extends AbstractRemoteWrapper implements DAO<T> {
    protected RemoteDAO<T> stub = null;

    protected AbstractRemoteDAOWrapper(String url) {
        super(url);
    }

    protected abstract void connect() throws DAOException;

    /**
     * Checks if the product exits in the stock list
     * @return true if exists otherwise false
     */
    @Override
    public synchronized boolean exists(String number) throws DAOException {
        DEBUG.trace("RemoteDAOWrapper:exists()");
        return wrapRemote(() -> stub.exists(number));
    }

    @Override
    public synchronized List<T> search(String searchQuery) throws DAOException {
        DEBUG.trace("RemoteDAOWrapper:search()");
        return wrapRemote(() -> stub.search(searchQuery));
    }

    /**
     * Returns details about the product in the stock list
     * @return StockNumber, Description, Price, Quantity
     */
    @Override
    public synchronized T get(String number) throws DAOException {
        DEBUG.trace("RemoteDAOWrapper:get()");
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
        DEBUG.trace("RemoteDAOWrapper:update()");
        this.<Void>wrapRemote(() -> {
            stub.update(detail);
            return null;
        });
    }
}

