package middle;

import orders.OrderDAO;
import products.ProductDAO;
import orders.remote.RemoteOrderDAOWrapper;
import products.remote.RemoteProductDAOWrapper;

/** Provide access to middle tier components. */
public class RemoteMiddleFactory implements MiddleFactory {
    private String stockURL = "";
    private String orderURL = "";

    public void setStockURL(String url) {
        stockURL = url;
    }
    public void setOrderURL(String url) {
        orderURL = url;
    }

    /** {@return an object to access the product DAO via RMI} */
    @Override
    public ProductDAO makeStockDAO() {
        return new RemoteProductDAOWrapper(stockURL);
    }

    /** {@return an object to access the order DAO via RMI} */
    @Override
    public OrderDAO makeOrderDAO() {
        return new RemoteOrderDAOWrapper(orderURL);
    }
}

