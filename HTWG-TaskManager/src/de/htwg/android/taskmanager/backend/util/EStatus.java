package de.htwg.android.taskmanager.backend.util;

/**
 * This enum represents the Status of Tasks.
 * 
 * @author Filippelli, Gerhart, Gillet
 * 
 */
public enum EStatus {

	/**
	 * Needs Action is defined, for uncompleted tasks.
	 */
	NEEDS_ACTION,
	/**
	 * Completed is defined, for completed tasks.
	 */
	COMPLETED;

	/**
	 * The Google Tasks API NEEDS_ACTION literal (defined as String
	 * "needsAction").
	 */
	private static final String NEEDS_ACTION_STRING = "needsAction";

	/**
	 * The Google Tasks API COMPLETED literal (defined as String "completed").
	 */
	private static final String COMPLETED_STRING = "completed";

	/**
	 * Transforms a EStatus String representation (in String representation)
	 * into a EStatus Object
	 * 
	 * @param status
	 *            the google status (as String)
	 * @return the EStatus object
	 */
	public static String transformStatus(EStatus status) {
		if (status == null) {
			return null;
		}
		if (status.equals(EStatus.NEEDS_ACTION)) {
			return NEEDS_ACTION_STRING;
		} else if (status.equals(EStatus.COMPLETED)) {
			return COMPLETED_STRING;
		} else {
			return null;
		}
	}

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
}
