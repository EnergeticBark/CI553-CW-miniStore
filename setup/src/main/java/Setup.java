import dbAccess.DBAccess;
import dbAccess.DBAccessFactory;

import java.sql.*;
import java.util.ArrayList;

/**
 * Repopulate the database with test data
 * @author  Mike Smith University of Brighton
 * @version 3.0 Derby
 */
class Setup {
    // SQL code to set up database tables.
    private static final String[] sqlStatements = {
            "DROP TABLE ProductTable",
            "CREATE TABLE ProductTable ("
                    + "productNo   CHAR(4),"
                    + "description VARCHAR(40),"
                    + "picture     VARCHAR(80),"
                    + "price       FLOAT)",

            "INSERT INTO ProductTable VALUES ('0001', '40 inch LED HD TV', 'images/pic0001.jpg', 269.00)",
            "INSERT INTO ProductTable VALUES ('0002', 'DAB Radio',         'images/pic0002.jpg', 29.99)",
            "INSERT INTO ProductTable VALUES ('0003', 'Toaster',           'images/pic0003.jpg', 19.99)",
            "INSERT INTO ProductTable VALUES ('0004', 'Watch',             'images/pic0004.jpg', 29.99)",
            "INSERT INTO ProductTable VALUES ('0005', 'Digital Camera',    'images/pic0005.jpg', 89.99)",
            "INSERT INTO ProductTable VALUES ('0006', 'MP3 player',        'images/pic0006.jpg', 7.99)",
            "INSERT INTO ProductTable VALUES ('0007', '32Gb USB2 drive',   'images/pic0007.jpg', 6.99)",

            "DROP TABLE StockTable",
            "CREATE TABLE StockTable ("
                    + "productNo  CHAR(4),"
                    + "stockLevel INTEGER)",

            "INSERT INTO StockTable VALUES ('0001', 90)",
            "INSERT INTO StockTable VALUES ('0002', 20)",
            "INSERT INTO StockTable VALUES ('0003', 33)",
            "INSERT INTO StockTable VALUES ('0004', 10)",
            "INSERT INTO StockTable VALUES ('0005', 17)",
            "INSERT INTO StockTable VALUES ('0006', 15)",
            "INSERT INTO StockTable VALUES ('0007', 01)",

            "SELECT * FROM StockTable, ProductTable WHERE StockTable.productNo = ProductTable.productNo"
    };

    public static void main(String[] args) {
        Connection theCon = null; // Connection to database
        DBAccess dbDriver = null;
        DBAccessFactory.setAction("Create");
        System.out.println("Setup CatShop database of stock items");
        try {
            dbDriver = (new DBAccessFactory()).getNewDBAccess();
            dbDriver.loadDriver();
            theCon = DriverManager.getConnection(
                    dbDriver.urlOfDatabase(),
                    dbDriver.username(),
                    dbDriver.password()
            );
        } catch (SQLException e) {
            System.err.println("Problem with connection to " + dbDriver.urlOfDatabase());
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState:     " + e.getSQLState());
            System.out.println("VendorError:  " + e.getErrorCode());
            System.exit(-1);
        } catch (Exception e) {
            System.err.println("Can not load JDBC/ODBC driver.");
            System.exit(-1);
        }

        Statement stmt = null;
        try {
            stmt = theCon.createStatement();
        } catch (Exception e) {
            System.err.println("problems creating statement object");
        }

        // execute SQL commands to create table, insert data
        for (String sqlStatement: sqlStatements) {
            try {
                System.out.println(sqlStatement);
                switch (sqlStatement.charAt(0)) {
                    case '/':
                        System.out.println("------------------------------");
                        break;
                    case 'S':
                    case 'F':
                        query(stmt, dbDriver.urlOfDatabase(), sqlStatement);
                        break;
                    case '*':
                        if (sqlStatement.length() >= 2) {
                            switch (sqlStatement.charAt(1)) {
                                case 'c':
                                    theCon.commit();
                                    break;
                                case 'r':
                                    theCon.rollback();
                                    break;
                                case '+':
                                    theCon.setAutoCommit(true);
                                    break;
                                case '-':
                                    theCon.setAutoCommit(false);
                                    break;
                            }
                        }
                        break;
                    default:
                        stmt.execute(sqlStatement);
                }
                //System.out.println();
            } catch (Exception e) {
                System.out.println("problems with SQL sent to "
                        + dbDriver.urlOfDatabase() + "\n"
                        + sqlStatement + "\n"
                        + e.getMessage()
                );
            }
        }

        try {
            theCon.close();
        } catch (Exception e) {
            System.err.println("problems with close: " + e.getMessage());
        }
    }


    private static void query(Statement stmt, String url, String stm) {
        try {
            ResultSet res = stmt.executeQuery(stm);

            ArrayList<String> names = new ArrayList<>(10);

            ResultSetMetaData md = res.getMetaData();
            int cols = md.getColumnCount();

            for (int j = 1; j <= cols; j++) {
                String name = md.getColumnName(j);
                System.out.printf("%-14.14s ", name);
                names.add(name);
            }
            System.out.println();

            for (int j = 1; j <= cols; j++) {
                System.out.printf("%-14.14s ",  md.getColumnTypeName(j));
            }
            System.out.println();

            while (res.next()) {
                for (int j = 0; j < cols; j++) {
                    String name = names.get(j);
                    System.out.printf("%-14.14s ", res.getString(name));
                }
                System.out.println();
            }
        } catch (Exception e) {
            System.err.println("problems with SQL sent to " + url + "\n" + e.getMessage());
        }
    }

    private static String m(int len, String s) {
        if (s.length() >= len) {
            return s.substring(0, len - 1) + " ";
        } else {
            StringBuilder res = new StringBuilder(len);
            res.append(s);
            for (int i = s.length(); i < len; i++) {
                res.append(' ');
            }
            return res.toString();
        }
    }
}
