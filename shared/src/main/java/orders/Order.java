package orders;

import catalogue.Basket;

import java.io.Serial;
import java.io.Serializable;

/**
 * Wraps a Basket and its state into an order.
 */
public class Order implements Serializable {
    @Serial
    private static final long serialVersionUID = 20092507;
    public enum State {Waiting, BeingPacked, ToBeCollected}

    private final int orderNumber;
    private State state = State.Waiting;
    private final Basket basket; // For this basket

    public Order(int orderNumber, Basket basket) {
        this.orderNumber = orderNumber;
        this.basket = basket;
    }

    public Order(int orderNumber, State state, Basket basket) {
        this.orderNumber = orderNumber;
        this.state = state;
        this.basket = basket;
    }

    public int getOrderNumber() {
        return orderNumber;
    }
    public State getState() {
        return state;
    }
    public Basket getBasket() {
        return basket;
    }

    public void setState(State state) {
        this.state = state;
    }
}