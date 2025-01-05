package middle;

import java.rmi.RemoteException;

public abstract class AbstractRemoteWrapper {
    protected final String url;

    protected AbstractRemoteWrapper(String url) {
        this.url = url;
    }

    protected abstract <E extends Throwable> void connect() throws E;

    protected interface RemoteMethod<R, E extends Throwable> {
        R apply() throws E, RemoteException;
    }
}
