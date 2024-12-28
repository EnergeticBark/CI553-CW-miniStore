package dbAccess;

/**
 * Implements management of a Microsoft Access database.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
class WindowsAccess extends DBAccess {
    private static final String URL = "jdbc:odbc:cshop";
    private static final String DRIVER = "sun.jdbc.odbc.JdbcOdbcDriver";

    public void loadDriver() throws Exception {
        Class.forName(DRIVER);
    }

    public String urlOfDatabase() {
        return URL;
    }
}
