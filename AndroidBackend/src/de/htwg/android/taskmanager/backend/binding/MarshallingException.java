package de.htwg.android.taskmanager.backend.binding;

import java.io.IOException;

public class MarshallingException extends IOException {

	private static final long serialVersionUID = 4434138050561899878L;
	private Exception innerException;

	public MarshallingException(Exception innerException) {
		this.innerException = innerException;
	}

	public Exception getInnerException() {
		return innerException;
	}

	public void setInnerException(Exception innerException) {
		this.innerException = innerException;
	}

}
