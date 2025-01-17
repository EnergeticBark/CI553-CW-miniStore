package dao;

import java.io.Serial;

/** Exception thrown if there is an error when accessing the persistence layer. */
public class DAOException extends Exception {
    @Serial
    private static final long serialVersionUID = 1;
    public DAOException(String s) {
        super(s);
    }
}
