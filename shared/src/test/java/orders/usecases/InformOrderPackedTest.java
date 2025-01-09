package orders.usecases;

import catalogue.Basket;
import orders.Order;
import orders.exceptions.OrderInvalidStateException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InformOrderPackedTest {

    @Test
    void alreadyPacked() {
        MockOrderDAO mockOrderDAO = new MockOrderDAO();
        Order order = new Order(1, Order.State.ToBeCollected, new Basket());
        assertThrows(
                OrderInvalidStateException.class,
                () -> new InformOrderPacked(mockOrderDAO).run(order)
        );
    }

    @Test
    void notStartedPacking() {
        MockOrderDAO mockOrderDAO = new MockOrderDAO();
        Order order = new Order(1, Order.State.Waiting, new Basket());
        assertThrows(
                OrderInvalidStateException.class,
                () -> new InformOrderPacked(mockOrderDAO).run(order)
        );
    }

    @Test
    void pack() {
        MockOrderDAO mockOrderDAO = new MockOrderDAO();
        Order order = new Order(1, Order.State.BeingPacked, new Basket());
        assertDoesNotThrow(() -> new InformOrderPacked(mockOrderDAO).run(order));
    }
}