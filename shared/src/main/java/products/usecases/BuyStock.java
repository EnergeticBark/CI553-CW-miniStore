package products.usecases;

import products.Product;
import products.exceptions.ProductOutOfStockException;
import products.ProductDAO;
import middle.DAOException;

public class BuyStock {
    private final ProductDAO productDAO;

    public BuyStock(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public Product run(String productNumber, int amount) throws
            ProductOutOfStockException,
            DAOException {

        Product inventory = productDAO.get(productNumber);
        new EnsureEnoughStock().run(inventory, amount);
        Product customersProducts = inventory.take(amount);

        productDAO.update(inventory);
        return customersProducts;
    }
}
