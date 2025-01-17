package dao;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Wrapper for allowing the {@link RemoteDAO} interface to be used as a local {@link DAO}.
 * This class only catches {@link RemoteException}s and rethrows them as {@link DAOException}s
 * All CRUD operations are performed in the class that implements RemoteDAO.
 * @param <T> The type of the entities this DAO works with.
 * @param <U> The RemoteDAO we want to use as a local DAO.
 */
public abstract class RemoteDAOWrapper<T, U extends RemoteDAO<T>> implements DAO<T> {
    protected String url;
    protected U stub = null;

    protected RemoteDAOWrapper(String url) {
        this.url = url;
    }

    protected abstract void connect() throws DAOException;

    /**
     * Invokes a {@link RemoteDAO}'s method, wrapping its RemoteException in a DAOException.
     * @param remoteMethod the method to invoke.
     * @return Whatever the method remoteMethod would have returned.
     * @param <R> The type of value returned by the remoteMethod.
     * @throws DAOException If there was an error in the persistence layer, or if remoteMethod threw a RemoteException.
     */
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

    @Override
    public synchronized boolean exists(int identifier) throws DAOException {
        return wrapRemote(() -> stub.exists(identifier));
    }

    @Override
    public synchronized T get(int identifier) throws DAOException {
        return wrapRemote(() -> stub.get(identifier));
    }

    @Override
    public List<T> getAll() throws DAOException {
        return wrapRemote(() -> stub.getAll());
    }

    @Override
    public synchronized void create(T newEntity) throws DAOException {
        wrapRemote(() -> {
            stub.create(newEntity);
            return null;
        });
    }

    @Override
    public synchronized void update(T replacement) throws DAOException {
        wrapRemote(() -> {
            stub.update(replacement);
            return null;
        });
    }
}

