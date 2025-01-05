package products.usecases;

import products.Product;

public class AddStock {
    public void run(Product product, int amount) {
        product.add(amount);
    }
}
