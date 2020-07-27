package nl.ebay.creditlimittracker.exception.handlers;

/**
 * A wrapper for exceptions occurring while converting to and from JSON
 */
public final class FileParsingException extends RuntimeException {

    /**
     * Handles all exceptions that occur while reading and converting file
     *
     * @param message: custom message based on exception
     * @param cause:   wrapped exception
     */
    public FileParsingException(String message, Throwable cause) {
        super(message, cause);
    }

}
