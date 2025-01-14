package products.usecases;

import products.Product;
import products.exceptions.ProductDoesNotExistException;
import products.ProductDAO;
import dao.DAOException;

public class GetProductByNumber {
    private final ProductDAO productDAO;

    public GetProductByNumber(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public Product run(int productNumber) throws ProductDoesNotExistException, DAOException {
        if (!productDAO.exists(productNumber)) {
            throw new ProductDoesNotExistException();
        }
        return productDAO.get(productNumber);
    }
}
