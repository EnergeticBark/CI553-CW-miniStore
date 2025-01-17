package products.usecases;

import products.Product;
import products.exceptions.ProductOutOfStockException;
import products.ProductDAO;
import dao.DAOException;

/** Use case for buying a {@link Product}, ensuring there's enough in-stock and updating the persistence layer. */
public class BuyStock {
    private final ProductDAO productDAO;

    public BuyStock(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    /**
     * Buys a {@link Product}, ensuring there's enough in stock and updating the persistence layer.
     * @param productNumber the product number of the product we want to buy.
     * @param amount the amount to buy.
     * @return the product bought, with the appropriate quantity.
     * @throws ProductOutOfStockException if there wasn't enough of the product in-stock.
     * @throws DAOException if there was an error in the persistence layer.
     */
    public Product run(int productNumber, int amount) throws
            ProductOutOfStockException,
            DAOException {

        Product inventory = productDAO.get(productNumber);
        new EnsureEnoughStock().run(inventory, amount);
        Product customersProducts = inventory.take(amount);

        productDAO.update(inventory);
        return customersProducts;
    }
}
