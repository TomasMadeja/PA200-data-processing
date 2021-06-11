package cz.muni.fi.pa200.dataprocessing.exceptions;

public class UploadError extends RuntimeException {
    public UploadError(String message) {
        super(message);
    }

    public UploadError(String message, Throwable cause) {
        super(message, cause);
    }
}
