package remote;

import dbAccess.DBStockReader;
import middle.StockException;

import java.rmi.RemoteException;

// There can only be 1 ResultSet opened per statement
// so no simultaneous use of the statement object
// hence the synchronized methods

/**
 * Implements Read access to the stock list,
 * the stock list is held in a relational DataBase.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public class RemoteDBStockReader extends DBStockReader implements RemoteStockReader {
    public RemoteDBStockReader() throws RemoteException, StockException {
        super();
    }
}
