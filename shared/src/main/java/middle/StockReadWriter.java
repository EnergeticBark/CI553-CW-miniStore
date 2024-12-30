package middle;

import catalogue.Product;

/**
 * Interface for read/write access to the stock list.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */

public interface StockReadWriter extends StockReader {
    /**
     * Modifies Stock details for a given product number.
     * Information modified: Description, Price
     * @param detail Replace with this version of product
     * @throws middle.StockException if issue
     */
    void modifyStock(Product detail) throws StockException;
}
