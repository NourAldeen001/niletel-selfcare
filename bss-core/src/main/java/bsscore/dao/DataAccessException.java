package bsscore.dao;

import java.sql.SQLException;

public class DataAccessException extends RuntimeException {

    private static final String UNIQUE_VIOLATION = "23505";

    private final String sqlState;

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
        this.sqlState = (cause instanceof SQLException ex) ? ex.getSQLState() : null;
    }

    public boolean isUniqueViolation() {
        return UNIQUE_VIOLATION.equals(sqlState);
    }
}
