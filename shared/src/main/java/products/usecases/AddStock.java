package products.usecases;

import products.Product;

/** Use case that adds to a product's quantity. */
public class AddStock {
    /**
     * Adds to a product's quantity.
     * @param product the product to add to.
     * @param amount the amount to add.
     */
    public void run(Product product, int amount) {
        product.add(amount);
    }
}
