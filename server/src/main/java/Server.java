import catalogue.Product;
import dataaccess.DBStockDAO;
import middle.Names;
import orders.OrderProcessorImpl;
import remote.RemoteOrderProcessor;
import remote.RemoteDAO;

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

        String orderProcessorURL = args.length < 2
                ? Names.ORDER // default location
                : args[1]; // supplied location

        (new Server()).bind(stockReaderURL, orderProcessorURL);
    }

    private void bind(String stockReaderURL, String orderProcessorURL) {
        RemoteDAO<Product> theStockR; // Remote stock object
        RemoteOrderProcessor theOrder; // Remote order object
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
            theStockR = new DBStockDAO(); // Stock R
            UnicastRemoteObject.exportObject(theStockR, 0);
            Naming.rebind(stockReaderURL, theStockR); // bind to url
            System.out.println("DBStockDAO bound to: " + stockReaderURL); // Inform world

            theOrder = new OrderProcessorImpl(); // OrderProcessorImpl
            UnicastRemoteObject.exportObject(theOrder, 0);
            Naming.rebind(orderProcessorURL, theOrder); // bind to url
            System.out.println("OrderProcessorImpl bound to: " + orderProcessorURL); // Inform world
        } catch (Exception err) {
            System.out.println("Fail Server: " + err.getMessage()); // Variety of reasons
        }
    }
}
