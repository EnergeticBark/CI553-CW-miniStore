package usecases;

import catalogue.Product;

public class AddStock {
    public void run(Product product, int amount) {
        product.add(amount);
    }
}
