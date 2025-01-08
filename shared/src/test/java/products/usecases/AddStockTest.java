package products.usecases;

import org.junit.jupiter.api.Test;
import products.Product;

import static org.junit.jupiter.api.Assertions.*;

class AddStockTest {

    @Test
    void add() {
        Product product = new Product(1, "Example Product 1", 24, 14);
        new AddStock().run(product, 5);
        assertEquals(19, product.getQuantity());
    }
}