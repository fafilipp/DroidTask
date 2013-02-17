package de.htwg.android.taskmanager.util.constants;

/**
 * This interface defines constants used by this app generally.
 * 
 * @author Filippelli, Gerhart, Gillet
 * 
 */
public interface GoogleTaskConstants {

	String AUTH_TOKEN_TYPE = "Manage your tasks";
	String GOOGLE_ACCOUNT_TYPE = "com.google";
	String SERVER_API_KEY = "AIzaSyALNwJA_3Pm3KfsCjZhgzsJXx85GlKYmAI";
	String APPLICATION_NAME = "HTWG-Konstanz - Task Manager";

	String ACTIVITY_KEY_EDIT = "is-edit";
	String ACTIVITY_KEY_TASK_TITLE = "task-title";
	String ACTIVITY_KEY_TASK_ID = "task-id";

	String ACTIVITY_DIALOG_CANCEL = "Cancel";
	String ACTIVITY_DIALOG_ADD = "Add";
	String ACTIVITY_DIALOG_EDIT = "Edit";
	String ACTIVITY_LAYOUT_DUE = "Due date:";
	String ACTIVITY_LAYOUT_COMPLETION = "Completion date:";

	String ACTIVITY_DIALOG_TASK_EDIT = "Edit";
	String ACTIVITY_DIALOG_TASK_DELETE = "Delete";

	int REQUEST_CODE_NEW_ACTIVITY = 100;
	int REQUEST_CODE_EDIT_ACTIVITY = 101;
	int REQUEST_CODE_SHOW_ACTIVITY = 102;

}
