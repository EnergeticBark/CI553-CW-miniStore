package dataaccess;

/**
 * Implements management of an Apache Derby database.
 *  that is too be created
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
class DerbyCreateAccess extends DBAccess {
    private static final String URL = "jdbc:derby:catshop.db;create=true";
    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

    public void loadDriver() throws Exception {
        Class.forName(DRIVER).getDeclaredConstructor().newInstance();
    }

    public String urlOfDatabase() {
        return URL;
    }
}

