package orders.exceptions;

/**
 * Exception thrown if the {@link orders.usecases.InformOrderPacked} use case is invoked on an {@link orders.Order} that
 * isn't in the {@code BeingPacked} state.
 */
public class OrderInvalidStateException extends Exception {
    public OrderInvalidStateException (String message) {
        super(message);
    }
}
