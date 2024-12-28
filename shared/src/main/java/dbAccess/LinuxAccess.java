package dbAccess;

/**
 * Implements management of an mySQL database on Linux.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
class LinuxAccess extends DBAccess {
    private static final String URL = "jdbc:mysql://localhost/cshop?user=root";
    private static final String DRIVER = "org.gjt.mm.mysql.Driver";

    public void loadDriver() throws Exception {
        Class.forName(DRIVER).getDeclaredConstructor().newInstance();
    }

    public String urlOfDatabase() {
        return URL;
    }
}
