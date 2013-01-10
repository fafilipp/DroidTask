package de.htwg.android.taskmanager.google.task.api.util;

/**
 * This class wraps thrown exceptions by the sync-operation.
 * 
 * @author Filippelli, Gerhart, Gillet
 * 
 */
public class GoogleSyncException extends Exception {

	/**
	 * The serial version uid for this exception.
	 */
	private static final long serialVersionUID = -1284211423890284018L;

	/**
	 * The wrapped inner exception.
	 */
	private Exception innerException;

	/**
	 * Creates a GoogleSyncException for a given inner exception.
	 * 
	 * @param innerException
	 */
	public GoogleSyncException(Exception innerException) {
		this.innerException = innerException;
	}

	/**
	 * Gets the cause of the inner exception
	 */
	@Override
	public Throwable getCause() {
		return innerException.getCause();
	}

	/**
	 * Gets the wrapped inner exception.
	 * 
	 * @return the wrapped inner exception.
	 */
	public Exception getInnerException() {
		return innerException;
	}

	/**
	 * Gets the localized message of the inner exception.
	 */
	@Override
	public String getLocalizedMessage() {
		return innerException.getLocalizedMessage();
	}

	/**
	 * Gets the message of the inner exception.
	 */
	@Override
	public String getMessage() {
		return innerException.getMessage();
	}

	/**
	 * Gets the stack trace of the inner exception.
	 */
	@Override
	public StackTraceElement[] getStackTrace() {
		return innerException.getStackTrace();
	}

}
