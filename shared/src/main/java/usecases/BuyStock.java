package usecases;

import catalogue.Product;
import exceptions.ProductOutOfStockException;
import middle.DAO;
import middle.StockException;

public class BuyStock {
    private final DAO<Product> stockDAO;

    public BuyStock(DAO<Product> stockDAO) {
        this.stockDAO = stockDAO;
    }

    public Product run(String productNumber, int amount) throws
            ProductOutOfStockException,
            StockException {

        Product inventory = stockDAO.get(productNumber);
        new EnsureEnoughStock().run(inventory, amount);
        Product customersProducts = inventory.take(amount);

        stockDAO.update(inventory);
        return customersProducts;
    }
}
