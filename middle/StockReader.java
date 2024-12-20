package middle;

import catalogue.Product;

import javax.swing.*;
import java.util.List;

/**
 * Interface for read access to the stock list.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public interface StockReader {

    /**
     * Checks if the product exits in the stock list
     * @param pNum Product number
     * @return true if exists otherwise false
     * @throws StockException if issue
     */
    boolean exists(String pNum) throws StockException;

    /**
     * Search for products based on keywords in their descriptions.
     * @param searchQuery The search query e.g. "TV", "Radio" or "Watch".
     * @return A possibly empty list of search results.
     * @throws StockException If there was an issue.
     */
    List<Product> searchByDescription(String searchQuery) throws StockException;

    /**
     * Returns details about the product in the stock list
     * @param pNum Product number
     * @return StockNumber, Description, Price, Quantity
     * @throws StockException if issue
     */
    Product getDetails(String pNum) throws StockException;

    /**
     * Returns an image of the product in the stock list
     * @param pNum Product number
     * @return Image
     * @throws StockException if issue
     */
    ImageIcon getImage(String pNum) throws StockException;
}
