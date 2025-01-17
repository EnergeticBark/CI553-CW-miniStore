package products.usecases;

import products.Product;
import products.exceptions.ProductDoesNotExistException;
import products.ProductDAO;
import dao.DAOException;

/** Use case that retrieves a product by product number from the persistence layer. */
public class GetProductByNumber {
    private final ProductDAO productDAO;

    public GetProductByNumber(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    /**
     * Retrieves a product by product number from the persistence layer.
     * @param productNumber product number of the desired product.
     * @return the product with the provided product number.
     * @throws ProductDoesNotExistException if no product with the provided product number exists.
     * @throws DAOException if there was an error in the persistence layer.
     */
    public Product run(int productNumber) throws ProductDoesNotExistException, DAOException {
        if (!productDAO.exists(productNumber)) {
            throw new ProductDoesNotExistException();
        }
        return productDAO.get(productNumber);
    }
}
