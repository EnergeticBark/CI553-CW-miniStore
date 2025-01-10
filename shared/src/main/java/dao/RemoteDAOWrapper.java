package dao;

import java.rmi.RemoteException;

/**
 * Facade for read access to the stock list.
 * The actual implementation of this is held on the middle tier.
 * The actual stock list is held in a relational DataBase on the
 * third tier.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public abstract class RemoteDAOWrapper<T, U extends RemoteDAO<T>> implements DAO<T> {
    protected String url;
    protected U stub = null;

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
    public synchronized boolean exists(int identifier) throws DAOException {
        return wrapRemote(() -> stub.exists(identifier));
    }

    /**
     * Returns details about the product in the stock list
     * @return StockNumber, Description, Price, Quantity
     */
    @Override
    public synchronized T get(int identifier) throws DAOException {
        return wrapRemote(() -> stub.get(identifier));
    }

    @Override
    public synchronized void create(T newEntity) throws DAOException {
        wrapRemote(() -> {
            stub.create(newEntity);
            return null;
        });
    }

    /**
     * Modifies Stock details for a given product number.
     * Information modified: Description, Price
     * @param detail Replace with this version of product
     * @throws DAOException if issue
     */
    @Override
    public synchronized void update(T detail) throws DAOException {
        wrapRemote(() -> {
            stub.update(detail);
            return null;
        });
    }
}

