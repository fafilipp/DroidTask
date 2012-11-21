package de.htwg.android.taskmanager.backend.database;

/**
 * This interface defines the constants used by the DatabaseHandler class.
 * 
 * @author Filippelli, Gerhart, Gillet
 * 
 */
public interface DatabaseConstants {

	// Database Version and Name
	int DATABASE_VERSION = 4;
	String DATABASE_NAME = "HTWG_TaskMngr-DB";

	// Table Name
	String TABLE_TASKLISTS = "htwg_tasklists";
	String TABLE_TASKS = "htwg_tasks";

	// Contacts Table Columns names
	String KEY_INTERNAL_ID = "internal_id";
	String KEY_GOOGLE_ID = "google_id";
	String KEY_LAST_MODIFICATION = "last_modification";
	String KEY_DELETE_FLAG = "delete_flag";
	String KEY_TITLE = "title";
	String KEY_TASK_NOTES = "notes";
	String KEY_TASK_STATUS = "status";
	String KEY_TASK_DUE = "due";
	String KEY_TASK_COMPLETED = "completed";
	String KEY_TASK_TASKLIST_ID = "tasklist_id";

	// Arrays of all columns
	String[] KEYS_TASKLISTS_TABLE = new String[] { KEY_INTERNAL_ID, KEY_GOOGLE_ID, KEY_LAST_MODIFICATION, KEY_TITLE };
	String[] KEYS_TASKS_TABLE = new String[] { KEY_INTERNAL_ID, KEY_GOOGLE_ID, KEY_LAST_MODIFICATION, KEY_TITLE, KEY_TASK_NOTES,
			KEY_TASK_STATUS, KEY_TASK_DUE, KEY_TASK_COMPLETED, KEY_TASK_TASKLIST_ID };

}
