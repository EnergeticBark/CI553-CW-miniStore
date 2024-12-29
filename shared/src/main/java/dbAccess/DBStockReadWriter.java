package dbAccess;

import catalogue.Product;
import debug.DEBUG;
import middle.StockException;
import middle.StockReadWriter;
import remote.RemoteStockReadWriter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

// There can only be 1 ResultSet opened per statement
// so no simultaneous use of the statement object
// hence the synchronized methods
// 

/**
 * Implements read/write access to the stock list
 * The stock list is held in a relational database
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public class DBStockReadWriter extends DBStockReader implements StockReadWriter, RemoteStockReadWriter {
    /*
     * Connects to database
     */
    public DBStockReadWriter() throws StockException {
        super(); // Connection done in DBStockReader's constructor
    }

    /**
     * Customer buys stock, quantity decreased if successful.
     * @param pNum Product number
     * @param amount Amount of stock bought
     * @return true if succeeds else false
     */
    public synchronized boolean buyStock(String pNum, int amount) throws StockException {
        final String query = """
                UPDATE StockTable SET stockLevel = stockLevel - ?
                WHERE productNo = ? AND stockLevel >= ?
                """;

        DEBUG.trace("DB DBStockReadWriter: buyStock(%s,%d)", pNum, amount);
        int updates;
        try (PreparedStatement statement = getConnectionObject().prepareStatement(query)) {
            statement.setInt(1, amount);
            statement.setString(2, pNum);
            statement.setInt(3, amount);
            statement.executeUpdate();
            updates = statement.getUpdateCount();
        } catch (SQLException e) {
            throw new StockException("SQL buyStock: " + e.getMessage());
        }
        DEBUG.trace("buyStock() updates -> %n", updates);
        return updates > 0; // success?
    }

    /**
     * Adds stock (Re-stocks) to the store.
     *  Assumed to exist in database.
     * @param pNum Product number
     * @param amount Amount of stock to add
     */
    public synchronized void addStock(String pNum, int amount) throws StockException {
        final String query = """
                UPDATE StockTable SET stockLevel = stockLevel + ?
                WHERE productNo = ?
                """;

        try (PreparedStatement statement = getConnectionObject().prepareStatement(query)) {
            statement.setInt(1, amount);
            statement.setString(2, pNum);
            statement.executeUpdate();
            DEBUG.trace("DB DBStockReadWriter: addStock(%s,%d)", pNum, amount);
        } catch (SQLException e) {
            throw new StockException("SQL addStock: " + e.getMessage());
        }
    }

    /**
     * Modifies Stock details for a given product number.
     *  Assumed to exist in database.
     * Information modified: Description, Price
     * @param detail Product details to change stock list to
     */
    public synchronized void modifyStock(Product detail) throws StockException {
        final String insertProductQuery = "INSERT INTO ProductTable VALUES (?, ?, ?, ?)";
        final String insertStockQuery = "INSERT INTO StockTable VALUES (?, ?)";

        final String updateProductQuery = "UPDATE ProductTable SET description = ?, price = ? WHERE productNo = ?";
        final String updateStockQuery = "UPDATE StockTable SET stockLevel = ? WHERE productNo = ?";

        DEBUG.trace("DB DBStockReadWriter: modifyStock(%s)", detail.getProductNumber());
        try {
            if (!exists(detail.getProductNumber())) {
                try (
                        PreparedStatement insertProductStatement = getConnectionObject()
                                .prepareStatement(insertProductQuery);
                        PreparedStatement insertStockStatement = getConnectionObject()
                                .prepareStatement(insertStockQuery)
                ) {
                    insertProductStatement.setString(1, detail.getProductNumber());
                    insertProductStatement.setString(2, detail.getDescription());
                    insertProductStatement.setString(
                            3,
                            "images/Pic" + detail.getProductNumber() + ".jpg"
                    );
                    insertProductStatement.setDouble(
                            4,
                            detail.getPrice()
                    );
                    insertProductStatement.executeUpdate();

                    insertStockStatement.setString(1, detail.getProductNumber());
                    insertStockStatement.setInt(2, detail.getQuantity());
                    insertStockStatement.executeUpdate();
                }
            } else {
                try (
                        PreparedStatement updateProductStatement = getConnectionObject()
                                .prepareStatement(updateProductQuery);
                        PreparedStatement updateStockStatement = getConnectionObject()
                                .prepareStatement(updateStockQuery)
                ) {
                    updateProductStatement.setString(1, detail.getDescription());
                    updateProductStatement.setDouble(2, detail.getPrice());
                    updateProductStatement.setString(3, detail.getProductNumber());
                    updateProductStatement.executeUpdate();

                    updateStockStatement.setInt(1, detail.getQuantity());
                    updateStockStatement.setString(2, detail.getProductNumber());
                    updateStockStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new StockException("SQL modifyStock: " + e.getMessage());
        }
    }
}
