package usecases;

import catalogue.Product;
import exceptions.ProductOutOfStockException;
import middle.ProductDAO;
import middle.DAOException;

public class BuyStock {
    private final ProductDAO stockDAO;

    public BuyStock(ProductDAO stockDAO) {
        this.stockDAO = stockDAO;
    }

    public Product run(String productNumber, int amount) throws
            ProductOutOfStockException,
            DAOException {

        Product inventory = stockDAO.get(productNumber);
        new EnsureEnoughStock().run(inventory, amount);
        Product customersProducts = inventory.take(amount);

        stockDAO.update(inventory);
        return customersProducts;
    }
}
