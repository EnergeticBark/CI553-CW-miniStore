package dataaccess;

/**
 * Apache Derby database access
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
class DerbyAccess extends DBAccess {
    private static final String URL = "jdbc:derby:catshop.db";
    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

    /** Load the Apache Derby database driver */
    public void loadDriver() throws Exception {
        Class.forName(DRIVER).getDeclaredConstructor().newInstance();
    }

    /** {@return the url to access the database} */
    public String urlOfDatabase() {
        return URL;
    }
}

