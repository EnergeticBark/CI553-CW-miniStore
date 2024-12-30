package usecases;

import catalogue.Product;
import exceptions.ProductOutOfStockException;
import middle.StockException;
import middle.StockReadWriter;

public class BuyStock {
    private final StockReadWriter stockReadWriter;

    public BuyStock(StockReadWriter stockReadWriter) {
        this.stockReadWriter = stockReadWriter;
    }

    public Product run(String productNumber, int amount) throws
            ProductOutOfStockException,
            StockException {

        Product inventory = stockReadWriter.getDetails(productNumber);
        new EnsureEnoughStock().run(inventory, amount);
        Product customersProducts = inventory.take(amount);

        stockReadWriter.modifyStock(inventory);
        return customersProducts;
    }
}
