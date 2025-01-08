package dao;

import java.rmi.RemoteException;

public interface RemoteMethod<R, E extends Throwable> {
    R apply() throws E, RemoteException;
}
