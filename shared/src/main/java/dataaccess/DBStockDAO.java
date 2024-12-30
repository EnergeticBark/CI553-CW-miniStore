package dataaccess;

import catalogue.Product;
import debug.DEBUG;
import middle.StockException;
import middle.StockDAO;
import remote.RemoteStockDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// There can only be 1 ResultSet opened per statement
// so no simultaneous use of the statement object
// hence the synchronized methods

// mySQL
//    no spaces after SQL statement ;

/**
 * Implements read access to the stock list
 * The stock list is held in a relational database
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public class DBStockDAO implements StockDAO, RemoteStockDAO {
    final private Connection theCon; // Connection to database

    /**
     * Connects to database
     * Uses a factory method to help set up the connection
     * @throws StockException if problem
     */
    public DBStockDAO() throws StockException {
        try {
            DBAccess dbDriver = (new DBAccessFactory()).getNewDBAccess();
            dbDriver.loadDriver();

            theCon = DriverManager.getConnection(
                    dbDriver.urlOfDatabase(),
                    dbDriver.username(),
                    dbDriver.password()
            );
            
            theCon.setAutoCommit(true);
        } catch (SQLException e) {
            throw new StockException("SQL problem:" + e.getMessage());
        } catch (Exception e) {
            throw new StockException("Can not load database driver.");
        }
    }

    /**
     * Returns a connection object that is used to process
     * requests to the DataBase
     * @return a connection object
     */
    protected Connection getConnectionObject() {
        return theCon;
    }

    /**
     * Checks if the product exits in the stock list
     * @param pNum The product number
     * @return true if exists otherwise false
     */
    public synchronized boolean exists(String pNum) throws StockException {
        final String query = "SELECT price FROM ProductTable WHERE ProductTable.productNo = ?";

        try (PreparedStatement statement = getConnectionObject().prepareStatement(query)) {
            statement.setString(1, pNum);
            ResultSet rs = statement.executeQuery();

            boolean res = rs.next();
            DEBUG.trace("DB DBStockDAO: exists(%s) -> %s", pNum, ( res ? "T" : "F" ));
            return res;
        } catch (SQLException e) {
            throw new StockException("SQL exists: " + e.getMessage());
        }
    }

    public synchronized List<Product> searchByDescription(String searchQuery) throws StockException {
        // Make the search case-insensitive by converting the description and search query to uppercase.
        final String query = """
                SELECT StockTable.productNo, description, picture, price, stockLevel
                FROM ProductTable, StockTable
                WHERE UPPER(description) LIKE UPPER(?)
                AND StockTable.productNo = ProductTable.productNo
                """;
        List<Product> foundProducts = new ArrayList<>();

        try (PreparedStatement statement = getConnectionObject().prepareStatement(query)) {
            statement.setString(1, "%" + searchQuery + "%");
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                final Product product = new Product(
                        rs.getString("productNo"),
                        rs.getString("description"),
                        rs.getString("picture"),
                        rs.getDouble("price"),
                        rs.getInt("stockLevel")
                );

                foundProducts.add(product);
            }
        } catch (SQLException e) {
            throw new StockException("SQL searchByDescription: " + e.getMessage());
        }

        return foundProducts;
    }

    /**
     * Returns details about the product in the stock list.
     * Assumed to exist in database.
     * @param pNum The product number
     * @return Details in an instance of a Product
     */
    public synchronized Product getDetails(String pNum) throws StockException {
        final String query = """
                SELECT description, picture, price, stockLevel
                FROM ProductTable, StockTable
                WHERE ProductTable.productNo = ?
                AND StockTable.productNo = ProductTable.productNo
                """;

        try (PreparedStatement statement = getConnectionObject().prepareStatement(query)) {
            statement.setString(1, pNum);
            ResultSet rs = statement.executeQuery();

            rs.next();
            return new Product(
                    pNum,
                    rs.getString("description"),
                    rs.getString("picture"),
                    rs.getDouble("price"),
                    rs.getInt("stockLevel")
            );
        } catch (SQLException e) {
            throw new StockException("SQL getDetails: " + e.getMessage());
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
