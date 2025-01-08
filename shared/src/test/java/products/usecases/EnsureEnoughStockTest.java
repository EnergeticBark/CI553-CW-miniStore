package products.usecases;

import products.Product;
import products.exceptions.ProductOutOfStockException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnsureEnoughStockTest {

    @Test
    void outOfStock() {
        Product product = new Product(1, "Example Product 1", 24, 14);
        assertThrows(
                ProductOutOfStockException.class,
                () -> new EnsureEnoughStock().run(product, 24)
        );
    }

    @Test
    void inStock() {
        Product product = new Product(1, "Example Product 1", 24, 14);
        assertDoesNotThrow(() -> new EnsureEnoughStock().run(product, 10));
    }

    @Test
    void oneLeft() {
        Product product = new Product(1, "Example Product 1", 24, 1);
        assertDoesNotThrow(() -> new EnsureEnoughStock().run(product, 1));
    }

    @Test
    void zeroLeft() {
        Product product = new Product(1, "Example Product 1", 24, 0);
        assertThrows(
                ProductOutOfStockException.class,
                () -> new EnsureEnoughStock().run(product, 1)
        );
    }
}