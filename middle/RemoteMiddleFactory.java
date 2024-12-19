/**
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
package middle;

/**
 * Provide access to middle tier components.
 */
public class RemoteMiddleFactory implements MiddleFactory {
    private String stockReaderURL = "";
    private String stockReadWriterURL = "";
    private String orderURL = "";

    public void setStockRInfo(String url) {
        stockReaderURL = url;
    }

    public void setStockRWInfo(String url) {
        stockReadWriterURL = url;
    }

    public void setOrderInfo(String url) {
        orderURL = url;
    }

    /**
     * Return an object to access the database for read only access.
     * Access is via RMI
     */
    public StockReader makeStockReader() {
        return new StockReaderProvider(stockReaderURL);
    }

    /**
     * Return an object to access the database for read/write access.
     * Access is via RMI
     */
    public StockReadWriter makeStockReadWriter() {
        return new StockReadWriterProvider(stockReadWriterURL);
    }

    /**
     * Return an object to access the order processing system.
     * Access is via RMI
     */
    public OrderProcessor makeOrderProcessing() {
        return new OrderProcessorProvider(orderURL);
    }
}

