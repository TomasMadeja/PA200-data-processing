package cz.muni.fi.pa200.dataprocessing.exceptions;

public class RetrievalError extends RuntimeException {
    public RetrievalError(String message) {
        super(message);
    }

    public RetrievalError(String message, Throwable cause) {
        super(message, cause);
    }
}
