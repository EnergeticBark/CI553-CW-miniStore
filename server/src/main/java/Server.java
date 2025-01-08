import orders.dataaccess.SQLOrderDAO;
import orders.remote.RemoteOrderDAO;
import products.dataaccess.SQLProductDAO;
import middle.Names;
import products.remote.RemoteProductDAO;

import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

/**
 * The server for the middle tier.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
class Server {
    public static void main(String[] args) {
        String stockReaderURL = args.length < 1
                ? Names.STOCK_DAO // default location
                : args[0]; // supplied location

        String orderDAOURL = args.length < 2
                ? Names.ORDER_DAO // default location
                : args[1]; // supplied location

        (new Server()).bind(stockReaderURL, orderDAOURL);
    }

    private void bind(String stockReaderURL, String orderDAOURL) {
        RemoteProductDAO theStockR; // Remote stock object
        RemoteOrderDAO orderDAO; // Remote order object
        System.out.println("Server: "); // Introduction
        try {
            LocateRegistry.createRegistry(1099);
            String ipAddress = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Server IP address " + ipAddress);
        } catch (Exception e) {
            System.out.println("Fail Starting RMI Registry" + e.getMessage());
            System.exit(0);
        }

        try {
            theStockR = new SQLProductDAO(); // Stock R
            UnicastRemoteObject.exportObject(theStockR, 0);
            Naming.rebind(stockReaderURL, theStockR); // bind to url
            System.out.println("SQLProductDAO bound to: " + stockReaderURL); // Inform world

            orderDAO = new SQLOrderDAO();
            UnicastRemoteObject.exportObject(orderDAO, 0);
            Naming.rebind(orderDAOURL, orderDAO); // bind to url
            System.out.println("SQLOrderDAO bound to: " + orderDAOURL); // Inform world
        } catch (Exception err) {
            System.out.println("Fail Server: " + err.getMessage()); // Variety of reasons
        }
    }
}
