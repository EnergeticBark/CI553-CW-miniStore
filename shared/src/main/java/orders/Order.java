package orders;

import catalogue.Basket;

/**
 * Wraps a Basket and its state into an order.
 */
public class Order {
    public enum State {Waiting, BeingPacked, ToBeCollected}

    private State state;
    private final Basket basket; // For this basket

    public Order(Basket basket) {
        state = State.Waiting;
        this.basket = basket;
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