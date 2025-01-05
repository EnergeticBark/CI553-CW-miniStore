/**
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
package middle;

import orders.OrderProcessor;
import orders.OrderProcessorProvider;
import products.ProductDAO;
import products.remote.RemoteProductDAOWrapper;

/**
 * Provide access to middle tier components.
 */
public class RemoteMiddleFactory implements MiddleFactory {
    private String stockURL = "";
    private String orderURL = "";

    public void setStockURL(String url) {
        stockURL = url;
    }
    public void setOrderURL(String url) {
        orderURL = url;
    }

    /**
     * Return an object to access the database for read only access.
     * Access is via RMI
     */
    public ProductDAO makeStockDAO() {
        return new RemoteProductDAOWrapper(stockURL);
    }

    /**
     * Return an object to access the order processing system.
     * Access is via RMI
     */
    public OrderProcessor makeOrderProcessing() {
        return new OrderProcessorProvider(orderURL);
    }
}

