package products.usecases;

import products.Product;
import products.exceptions.ProductDoesNotExistException;
import products.ProductDAO;
import dao.DAOException;

/** Use case that adds to a products stock and updates it in the persistence layer. */
public class RestockProduct {
    private final ProductDAO productDAO;

    public RestockProduct(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    /**
     * Adds to a products stock and updates it in the persistence layer.
     * @param productNumber the product number of the product whose stock we want to add to.
     * @param amount the amount to add to the provided product.
     * @return the product, with its quantity updated.
     * @throws ProductDoesNotExistException if no product exists with the provided product number.
     * @throws DAOException if there was an error in the persistence layer.
     */
    public Product run(int productNumber, int amount) throws ProductDoesNotExistException, DAOException {
        Product inventory = new GetProductByNumber(productDAO).run(productNumber);
        new AddStock().run(inventory, amount);

        productDAO.update(inventory);
        return inventory;
    }
}
