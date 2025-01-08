package products.usecases;

import products.Product;
import products.exceptions.ProductDoesNotExistException;
import products.ProductDAO;
import dao.DAOException;

import java.util.List;

public class GetProductsBySearch {
    private final ProductDAO productDAO;

    public GetProductsBySearch(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public List<Product> run(String productNumber) throws ProductDoesNotExistException, DAOException {
        List<Product> products = productDAO.search(productNumber);
        if (products.isEmpty()) {
            throw new ProductDoesNotExistException();
        }
        return products;
    }
}
