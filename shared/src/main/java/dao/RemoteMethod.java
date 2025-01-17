package dao;

import java.rmi.RemoteException;

/**
 * The very generic RemoteMethod functional interface.
 * @param <R> The type returned by calling a remote method.
 * @param <E> The exception we expect a remote method to throw aside from RemoteException, usually DAOException.
 */
public interface RemoteMethod<R, E extends Throwable> {
    R apply() throws E, RemoteException;
}
