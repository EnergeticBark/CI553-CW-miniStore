package catalogue;

import products.Product;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BetterBasketTest {

    @Test
    void merge() {
        Product product1 = new Product(1, "Example Product 1", 100, 1);
        Product product2 = new Product(2, "Example Product 2", 25, 1);
        Product product3 = new Product(1, "Example Product 1", 100, 1);
        Product product4 = new Product(1, "Example Product 1", 100, 1);

        BetterBasket betterBasket = new BetterBasket();
        betterBasket.add(product1);
        betterBasket.add(product2);
        betterBasket.add(product3);
        betterBasket.add(product4);
        assertEquals(2, betterBasket.size(), "Incorrect size after merge");
        assertEquals(3, betterBasket.getFirst().getQuantity(), "Incorrect quantity after merge");
    }

    @Test
    void sort() {
        Product product1 = new Product(2, "Example Product 2", 25, 1);
        Product product2 = new Product(4, "Example Product 4", 75, 1);
        Product product3 = new Product(4, "Example Product 4", 75, 1);
        Product product4 = new Product(1, "Example Product 1", 100, 1);

        BetterBasket betterBasket = new BetterBasket();
        betterBasket.add(product1);
        betterBasket.add(product2);
        betterBasket.add(product3);
        betterBasket.add(product4);
        assertEquals(1, betterBasket.getFirst().getProductNumber(), "Incorrect position after sort");
    }
}