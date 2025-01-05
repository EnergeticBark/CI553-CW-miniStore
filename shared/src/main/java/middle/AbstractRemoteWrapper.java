package middle;

import java.rmi.Remote;
import java.rmi.RemoteException;

public abstract class AbstractRemoteWrapper {
    protected Remote stub = null;
    protected final String url;

    protected AbstractRemoteWrapper(String url) {
        this.url = url;
    }

    abstract void connect() throws DAOException;

    interface RemoteMethod<R, E extends Throwable> {
        R apply() throws E, RemoteException;
    }

     <R> R wrapRemote(RemoteMethod<R, DAOException> remoteMethod) throws DAOException {
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
}
