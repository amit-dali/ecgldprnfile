package nl.ebay.creditlimittracker.exception.handlers;

/**
 * A wrapper for exceptions occurring while converting to and from JSON
 */
public final class JsonConversionException extends RuntimeException {

	/**
	 * Handles all exceptions that occur while converting to and from JSON
	 *
	 * @param message: custom message based on exception
	 * @param cause: wrapped exception
	 */
	public JsonConversionException(String message, Throwable cause) {
		super(message, cause);
	}

}
