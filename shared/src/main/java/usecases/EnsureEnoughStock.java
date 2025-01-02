package usecases;

import catalogue.Product;
import exceptions.ProductOutOfStockException;

public class EnsureEnoughStock {
    public void run(Product product, int quantity) throws ProductOutOfStockException {
        if (product.getQuantity() < quantity) {
            throw new ProductOutOfStockException(product.getDescription());
        }
    }
}