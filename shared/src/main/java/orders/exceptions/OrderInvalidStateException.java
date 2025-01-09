package orders.exceptions;

public class OrderInvalidStateException extends Exception {
    public OrderInvalidStateException (String message) {
        super(message);
    }
}
