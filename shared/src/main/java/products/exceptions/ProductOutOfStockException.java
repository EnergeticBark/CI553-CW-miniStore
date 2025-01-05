package products.exceptions;

public class ProductOutOfStockException extends Exception {
    public ProductOutOfStockException(String description) {
        super(description);
    }
}
