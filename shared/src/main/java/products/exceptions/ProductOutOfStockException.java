package products.exceptions;

/** Exception thrown if a use case is invoked on a product that doesn't have enough stock. */
public class ProductOutOfStockException extends Exception {
    public ProductOutOfStockException(String description) {
        super(description);
    }
}
