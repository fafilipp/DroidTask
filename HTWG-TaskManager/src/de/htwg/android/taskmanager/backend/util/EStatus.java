package de.htwg.android.taskmanager.backend.util;

public enum EStatus {
	NEEDS_ACTION, COMPLETED;
	 
	private final static String NEEDS_ACTION_STRING = "needsAction";
	private final static String COMPLETED_STRING = "completed";
	
	/**
	 * Transforms a Google Status (in String representation) into a EStatus
	 * Object
	 * 
	 * @param status
	 *            the google status (as String)
	 * @return the EStatus object
	 */
	public static EStatus transformStatus(String status) {
		if (status.equals(NEEDS_ACTION_STRING)) {
			return EStatus.NEEDS_ACTION;
		} else if (status.equals(COMPLETED_STRING)) {
			return EStatus.COMPLETED;
		} else {
			return null;
		}
	}
	
	/**
	 * Transforms a EStatus String representation (in String representation) into a EStatus
	 * Object
	 * 
	 * @param status
	 *            the google status (as String)
	 * @return the EStatus object
	 */
	public static String transformStatus(EStatus status) {
		if (status == null) {
			return null;
		}
		if(status.equals(EStatus.NEEDS_ACTION)) {
			return NEEDS_ACTION_STRING;
		} else if (status.equals(EStatus.COMPLETED)) {
			return COMPLETED_STRING;
		} else {
			return null;
		}
	}
}
