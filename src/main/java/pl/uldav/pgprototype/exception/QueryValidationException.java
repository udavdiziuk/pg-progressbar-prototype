package pl.uldav.pgprototype.exception;

/**
 * Used when incorrect/not valid AST query received
 */
public class QueryValidationException extends Exception {
    public QueryValidationException(String message) {
        super(message);
    }
}
