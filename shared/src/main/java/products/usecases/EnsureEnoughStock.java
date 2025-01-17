package products.usecases;

import products.Product;
import products.exceptions.ProductOutOfStockException;

/** Use case that ensures there is enough of a product. */
public class EnsureEnoughStock {
    /**
     * Ensures there is enough of a product.
     * @param product the product to check.
     * @param quantity the amount desired.
     * @throws ProductOutOfStockException if there wasn't enough.
     */
    public void run(Product product, int quantity) throws ProductOutOfStockException {
        if (product.getQuantity() < quantity) {
            throw new ProductOutOfStockException(product.getDescription());
        }
    }
}
