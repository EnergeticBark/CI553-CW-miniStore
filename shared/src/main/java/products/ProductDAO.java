package products;

import dao.DAO;
import dao.DAOException;

import java.util.List;

public interface ProductDAO extends DAO<Product> {
    /**
     * Search for products based on keywords in their descriptions.
     * @param searchQuery The search query e.g. "TV", "Radio" or "Watch".
     * @return A possibly empty list of search results.
     * @throws DAOException If there was an issue.
     */
    List<Product> search(String searchQuery) throws DAOException;
}
