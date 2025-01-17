package products.exceptions;

/** Exception thrown if a use case is invoked with a product number that doesn't exist in the persistence layer. */
public class ProductDoesNotExistException extends Exception {
    public ProductDoesNotExistException() {
        super();
    }
}
