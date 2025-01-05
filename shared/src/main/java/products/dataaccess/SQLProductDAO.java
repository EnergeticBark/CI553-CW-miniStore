package products.dataaccess;

import dataaccess.DBAccess;
import dataaccess.DBAccessFactory;
import products.Product;
import debug.DEBUG;
import middle.DAOException;
import products.ProductDAO;
import products.remote.RemoteProductDAO;

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
public class SQLProductDAO implements ProductDAO, RemoteProductDAO {
    final private Connection theCon; // Connection to database

    /**
     * Connects to database
     * Uses a factory method to help set up the connection
     * @throws DAOException if problem
     */
    public SQLProductDAO() throws DAOException {
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
            throw new DAOException("SQL problem:" + e.getMessage());
        } catch (Exception e) {
            throw new DAOException("Can not load database driver.");
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
    @Override
    public synchronized boolean exists(String pNum) throws DAOException {
        final String query = "SELECT price FROM ProductTable WHERE ProductTable.productNo = ?";

        try (PreparedStatement statement = getConnectionObject().prepareStatement(query)) {
            statement.setString(1, pNum);
            ResultSet rs = statement.executeQuery();

            boolean res = rs.next();
            DEBUG.trace("DB SQLProductDAO: exists(%s) -> %s", pNum, ( res ? "T" : "F" ));
            return res;
        } catch (SQLException e) {
            throw new DAOException("SQL exists: " + e.getMessage());
        }
    }

    @Override
    public synchronized List<Product> search(String searchQuery) throws DAOException {
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
            throw new DAOException("SQL searchByDescription: " + e.getMessage());
        }

        return foundProducts;
    }

    /**
     * Returns details about the product in the stock list.
     * Assumed to exist in database.
     * @param pNum The product number
     * @return Details in an instance of a Product
     */
    @Override
    public synchronized Product get(String pNum) throws DAOException {
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
            throw new DAOException("SQL getDetails: " + e.getMessage());
        }
    }

    /**
     * Modifies Stock details for a given product number.
     *  Assumed to exist in database.
     * Information modified: Description, Price
     * @param detail Product details to change stock list to
     */
    @Override
    public synchronized void update(Product detail) throws DAOException {
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
            throw new DAOException("SQL modifyStock: " + e.getMessage());
        }
    }
}
