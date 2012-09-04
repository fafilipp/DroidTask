package de.htwg.android.taskmanager.google.sync;

public class GoogleSyncException extends Exception {

	private static final long serialVersionUID = -1284211423890284018L;
	private Exception innerException;

	public GoogleSyncException(Exception innerException) {
		this.innerException = innerException;
	}

	@Override
	public Throwable getCause() {
		return innerException.getCause();
	}

	public Exception getInnerException() {
		return innerException;
	}
	
	@Override
	public String getLocalizedMessage() {
		return innerException.getLocalizedMessage();
	}
	
	@Override
	public String getMessage() {
		return innerException.getMessage();
	}
	
	@Override
	public StackTraceElement[] getStackTrace() {
		return innerException.getStackTrace();
	}
	
	public void setInnerException(Exception innerException) {
		this.innerException = innerException;
	}
	
}
