package dataaccess;

/**
 * Implements generic management of a database.
 * Base class that defines the access to the database driver
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public class DBAccess {
    public void loadDriver() throws Exception {
        throw new RuntimeException("No driver");
    }

    public String urlOfDatabase() {
        return "";
    }

    public String username() {
        return "";
    }

    public String password() {
        return "";
    }
}
