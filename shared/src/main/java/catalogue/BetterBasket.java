package catalogue;

import products.Product;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;

/**
 * A collection of products to be purchased. Products are sorted by productNumber in ascending order.
 * Any duplicate products get automatically merged into one {@link Product} of the correct quantity.
 * @author Seth Humphries
 * @version 1.0
 */
public class BetterBasket extends Basket implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Add a product to the basket.
     * Unlike {@link Basket}, multiple instances with the same product number will be merged into a single
     * {@link Product} of appropriate quantity.
     * @param product Product to add, or merge if it's already in the basket.
     * @return true if the product was added successfully.
     */
    @Override
    public boolean add(Product product) {
        // Look for another Product in the basket with the same product number.
        Optional<Product> duplicate = this.stream()
                .filter((productInBasket) -> productInBasket.getProductNumber() == product.getProductNumber())
                .findAny();

        if (duplicate.isPresent()) {
            Product productInBasket = duplicate.get();
            int newQuantity = productInBasket.getQuantity() + product.getQuantity();
            productInBasket.setQuantity(newQuantity);
            return true; // Duplicates were merged and no sorting needed, so return early.
        }

        boolean success = super.add(product); // Add the product to this list normally.

        // Sort products after adding
        this.sort(Product.sortByNumber);
        return success;
    }
}
