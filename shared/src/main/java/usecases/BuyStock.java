package usecases;

import catalogue.Product;
import exceptions.ProductOutOfStockException;
import middle.StockDAO;
import middle.StockException;

public class BuyStock {
    private final StockDAO stockDAO;

    public BuyStock(StockDAO stockDAO) {
        this.stockDAO = stockDAO;
    }

    public Product run(String productNumber, int amount) throws
            ProductOutOfStockException,
            StockException {

        Product inventory = stockDAO.getDetails(productNumber);
        new EnsureEnoughStock().run(inventory, amount);
        Product customersProducts = inventory.take(amount);

        stockDAO.modifyStock(inventory);
        return customersProducts;
    }
}
