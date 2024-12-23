package middle;

import remote.RemoteOrder;
import remote.RemoteDBStockReadWriter;
import remote.RemoteDBStockReader;

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
                ? Names.STOCK_R // default location
                : args[0]; // supplied location

        String stockReadWriterURL = args.length < 2
                ? Names.STOCK_RW // default location
                : args[1]; // supplied location

        String orderProcessorURL = args.length < 3
                ? Names.ORDER // default location
                : args[2]; // supplied location

        (new Server()).bind(stockReaderURL, stockReadWriterURL, orderProcessorURL);
    }

    private void bind(String stockReaderURL, String stockReadWriterURL, String orderProcessorURL) {
        RemoteDBStockReader theStockR; // Remote stock object
        RemoteDBStockReadWriter theStockRW; // Remote stock object
        RemoteOrder theOrder; // Remote order object
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
            theStockR = new RemoteDBStockReader(); // Stock R
            UnicastRemoteObject.exportObject(theStockR, 0);
            Naming.rebind(stockReaderURL, theStockR); // bind to url
            System.out.println("DBStockReader bound to: " + stockReaderURL); // Inform world

            theStockRW = new RemoteDBStockReadWriter(); // Stock RW
            UnicastRemoteObject.exportObject(theStockRW, 0);
            Naming.rebind(stockReadWriterURL, theStockRW); // bind to url
            System.out.println("DBStockReadWriter bound to: " + stockReadWriterURL); // Inform world

            theOrder = new RemoteOrder(); // Order
            UnicastRemoteObject.exportObject(theOrder, 0);
            Naming.rebind(orderProcessorURL, theOrder); // bind to url
            System.out.println("Order bound to: " + orderProcessorURL); // Inform world
        } catch (Exception err) {
            System.out.println("Fail Server: " + err.getMessage()); // Variety of reasons
        }
    }
}
