package usecases;

import catalogue.Product;
import exceptions.ProductDoesNotExistException;
import middle.StockDAO;
import middle.StockException;

public class RestockProduct {
    private final StockDAO stockDAO;

    public RestockProduct(StockDAO stockReadWriter) {
        this.stockDAO = stockReadWriter;
    }

    public Product run(String productNumber, int amount) throws StockException, ProductDoesNotExistException {
        Product inventory = new GetProductByNumber(stockDAO).run(productNumber);
        new AddStock().run(inventory, amount);

        stockDAO.modifyStock(inventory);
        return inventory;
    }
}
