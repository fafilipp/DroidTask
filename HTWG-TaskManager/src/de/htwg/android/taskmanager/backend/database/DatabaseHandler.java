package de.htwg.android.taskmanager.backend.database;

import static de.htwg.android.taskmanager.backend.database.DatabaseConstants.DATABASE_NAME;
import static de.htwg.android.taskmanager.backend.database.DatabaseConstants.DATABASE_VERSION;
import static de.htwg.android.taskmanager.backend.database.DatabaseConstants.KEYS_TASKLISTS_TABLE;
import static de.htwg.android.taskmanager.backend.database.DatabaseConstants.KEYS_TASKS_TABLE;
import static de.htwg.android.taskmanager.backend.database.DatabaseConstants.KEY_DELETE_FLAG;
import static de.htwg.android.taskmanager.backend.database.DatabaseConstants.KEY_GOOGLE_ID;
import static de.htwg.android.taskmanager.backend.database.DatabaseConstants.KEY_INTERNAL_ID;
import static de.htwg.android.taskmanager.backend.database.DatabaseConstants.KEY_LAST_MODIFICATION;
import static de.htwg.android.taskmanager.backend.database.DatabaseConstants.KEY_TASK_COMPLETED;
import static de.htwg.android.taskmanager.backend.database.DatabaseConstants.KEY_TASK_DUE;
import static de.htwg.android.taskmanager.backend.database.DatabaseConstants.KEY_TASK_NOTES;
import static de.htwg.android.taskmanager.backend.database.DatabaseConstants.KEY_TASK_STATUS;
import static de.htwg.android.taskmanager.backend.database.DatabaseConstants.KEY_TASK_TASKLIST_ID;
import static de.htwg.android.taskmanager.backend.database.DatabaseConstants.KEY_TITLE;
import static de.htwg.android.taskmanager.backend.database.DatabaseConstants.TABLE_TASKLISTS;
import static de.htwg.android.taskmanager.backend.database.DatabaseConstants.TABLE_TASKS;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import de.htwg.android.taskmanager.backend.entity.LocalTask;
import de.htwg.android.taskmanager.backend.entity.LocalTaskList;
import de.htwg.android.taskmanager.backend.util.EStatus;

public class DatabaseHandler extends SQLiteOpenHelper {

	/**
	 * Creates a new Database Handler (calls just super method with defined
	 * constants).
	 * 
	 * @param context
	 *            the context activity
	 */
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * Adds a task to the database in a given tasklist
	 * 
	 * @param taskList
	 *            the tasklist where the task should be saved
	 * @param task
	 *            the task to add
	 */
	public void addTask(LocalTaskList taskList, LocalTask task) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = createContentValues(taskList, task);
		values.put(KEY_DELETE_FLAG, 0);
		db.insert(TABLE_TASKS, null, values);
		db.close();
	}

	/**
	 * Adds a tasklist the database
	 * 
	 * @param taskList
	 *            the tasklist to add
	 */
	public void addTaskList(LocalTaskList taskList) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = createContentValues(taskList);
		values.put(KEY_DELETE_FLAG, 0);
		db.insert(TABLE_TASKLISTS, null, values);
		db.close();
	}

	/**
	 * Creates the ContentValues for the add/edit of tasklists
	 * 
	 * @param taskList
	 *            the tasklists
	 * @return the contentvalues
	 */
	private ContentValues createContentValues(LocalTaskList taskList) {
		ContentValues values = new ContentValues();
		values.put(KEY_INTERNAL_ID, taskList.getInternalId());
		values.put(KEY_GOOGLE_ID, taskList.getGoogleId());
		values.put(KEY_LAST_MODIFICATION, taskList.getLastModification());
		values.put(KEY_TITLE, taskList.getTitle());
		return values;
	}

	/**
	 * Creates the ContentValues for the add/edit of tasks
	 * 
	 * @param taskList
	 *            the tasklist for this task
	 * @param task
	 *            the task
	 * @return the contentvalues
	 */
	private ContentValues createContentValues(LocalTaskList taskList, LocalTask task) {
		ContentValues values = new ContentValues();
		values.put(KEY_INTERNAL_ID, task.getInternalId());
		values.put(KEY_GOOGLE_ID, task.getGoogleId());
		values.put(KEY_LAST_MODIFICATION, task.getLastModification());
		values.put(KEY_TITLE, task.getTitle());
		values.put(KEY_TASK_NOTES, task.getNotes());
		values.put(KEY_TASK_STATUS, task.getStatus().toString());
		values.put(KEY_TASK_DUE, task.getDue());
		values.put(KEY_TASK_COMPLETED, task.getCompleted());
		if (taskList != null) {
			values.put(KEY_TASK_TASKLIST_ID, taskList.getInternalId());
		}
		return values;
	}

	/**
	 * Creates a LocalTaskList object for a database cursor result
	 * 
	 * @param cursor
	 *            the cursor result
	 * @return a LocalTaskList object
	 */
	private LocalTaskList createLocalTaskListObject(Cursor cursor) {
		LocalTaskList taskList = new LocalTaskList(cursor.getString(cursor.getColumnIndex(KEY_INTERNAL_ID)));
		taskList.setGoogleId(cursor.getString(cursor.getColumnIndex(KEY_GOOGLE_ID)));
		taskList.setLastModification(Long.parseLong(cursor.getString(cursor.getColumnIndex(KEY_LAST_MODIFICATION))));
		taskList.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
		for (LocalTask localTask : getTasks(taskList)) {
			taskList.addTaskToList(localTask);
		}
		return taskList;
	}

	/**
	 * Creates a LocalTask object for a database cursor result
	 * 
	 * @param cursor
	 *            the cursor result
	 * @return a LocalTask object
	 */
	private LocalTask createLocalTaskObject(Cursor cursor) {
		LocalTask task = new LocalTask(cursor.getString(cursor.getColumnIndex(KEY_INTERNAL_ID)));
		task.setGoogleId(cursor.getString(cursor.getColumnIndex(KEY_GOOGLE_ID)));
		task.setLastModification(Long.parseLong(cursor.getString(cursor.getColumnIndex(KEY_LAST_MODIFICATION))));
		task.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
		task.setNotes(cursor.getString(cursor.getColumnIndex(KEY_TASK_NOTES)));
		task.setStatus(EStatus.valueOf(cursor.getString(cursor.getColumnIndex(KEY_TASK_STATUS))));
		task.setDue(Long.parseLong(cursor.getString(cursor.getColumnIndex(KEY_TASK_DUE))));
		task.setCompleted(Long.parseLong(cursor.getString(cursor.getColumnIndex(KEY_TASK_COMPLETED))));
		return task;
	}

	/**
	 * Creates the tasklists table
	 * 
	 * @param db
	 *            the database where the table should be created
	 */
	private void createTasklistsTable(SQLiteDatabase db) {
		String CREATE_TASKLISTS_TABLE = "CREATE TABLE " + TABLE_TASKLISTS + "(" + KEY_INTERNAL_ID + " TEXT PRIMARY KEY," + KEY_GOOGLE_ID
				+ " TEXT UNIQUE," + KEY_LAST_MODIFICATION + " INTEGER," + KEY_TITLE + " TEXT," + KEY_DELETE_FLAG + " INTEGER)";
		db.execSQL(CREATE_TASKLISTS_TABLE);
	}

	/**
	 * Creates the task table
	 * 
	 * @param db
	 *            the database where the table should be created
	 */
	private void createTasksTable(SQLiteDatabase db) {
		String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "(" + KEY_INTERNAL_ID + " TEXT PRIMARY KEY," + KEY_GOOGLE_ID
				+ " TEXT UNIQUE," + KEY_LAST_MODIFICATION + " INTEGER," + KEY_TITLE + " TEXT," + KEY_TASK_NOTES + " TEXT,"
				+ KEY_TASK_STATUS + " TEXT," + KEY_TASK_DUE + " INTEGER," + KEY_TASK_COMPLETED + " INTEGER," + KEY_TASK_TASKLIST_ID
				+ " INTEGER," + KEY_DELETE_FLAG + " INTEGER)";
		db.execSQL(CREATE_TASKS_TABLE);
	}

	/**
	 * Deletes all tasks a given task list in the database (no real deletion, a
	 * delete flag will be set).
	 * 
	 * @param taskListInternalId
	 *            the internal id of the task list.
	 */
	public void deleteAllTasksForTaskList(String taskListInternalId) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_DELETE_FLAG, 1);
		db.update(TABLE_TASKS, values, KEY_TASK_TASKLIST_ID + " = ?", new String[] { taskListInternalId });
	}

	/**
	 * Deletes all tasks for a given task list in the database (real deletion).
	 * 
	 * @param taskListInternalId
	 *            the internal id of the task list.
	 */
	public void deleteAllTasksForTaskListFinal(String taskListInternalId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_TASKS, KEY_TASK_TASKLIST_ID + " = ?", new String[] { taskListInternalId });
		db.close();
	}

	/**
	 * Deletes a task from the database (no real deletion, a delete flag will be
	 * set).
	 * 
	 * @param taskInternalId
	 *            the internal id of the task to delete
	 */
	public void deleteTask(String taskInternalId) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_DELETE_FLAG, 1);
		db.update(TABLE_TASKS, values, KEY_INTERNAL_ID + " = ?", new String[] { taskInternalId });
	}

	/**
	 * Deletes a task from the database (real deletion).
	 * 
	 * @param taskInternalId
	 *            the internal id of the task to delete
	 */
	public void deleteTaskFinal(String taskInternalId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_TASKS, KEY_INTERNAL_ID + " = ?", new String[] { taskInternalId });
		db.close();
	}

	/**
	 * Deletes a task list from the database (no real deletion, a delete flag
	 * will be set).
	 * 
	 * @param taskListInternalId
	 *            the internal id of the task list to delete
	 */
	public void deleteTaskList(String taskListInternalId) {
		deleteAllTasksForTaskList(taskListInternalId);
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_DELETE_FLAG, 1);
		db.update(TABLE_TASKLISTS, values, KEY_INTERNAL_ID + " = ?", new String[] { taskListInternalId });
	}

	/**
	 * Deletes a task list from the database (real deletion).
	 * 
	 * @param taskListInternalId
	 *            the internal id of the task list to delete
	 */
	public void deleteTaskListFinal(String taskListInternalId) {
		deleteAllTasksForTaskListFinal(taskListInternalId);
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_TASKLISTS, KEY_INTERNAL_ID + " = ?", new String[] { String.valueOf(taskListInternalId) });
		db.close();
	}

	/**
	 * Gets all tasks which are been marked as deleted.
	 * 
	 * @return a list of the tasks marked as deleted.
	 */
	public List<LocalTask> getDeletedTask() {
		List<LocalTask> listOfTask = new ArrayList<LocalTask>();
		SQLiteDatabase db = this.getReadableDatabase();
		String TABLE = TABLE_TASKS;
		String[] FIELDS = KEYS_TASKS_TABLE;
		String WHERE = KEY_DELETE_FLAG + "=?";
		String[] WHERE_SELECTION = { String.valueOf(1) };
		Cursor cursor = db.query(TABLE, FIELDS, WHERE, WHERE_SELECTION, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			do {
				listOfTask.add(createLocalTaskObject(cursor));
			} while (cursor.moveToNext());
		}
		return listOfTask;
	}

	/**
	 * Gets a list of all task lists which are been marked as deleted
	 * 
	 * @return a list of all task lists which are been marked as deleted.
	 */
	public List<LocalTaskList> getDeletedTaskLists() {
		List<LocalTaskList> listOfTaskList = new ArrayList<LocalTaskList>();
		SQLiteDatabase db = this.getReadableDatabase();
		String TABLE = TABLE_TASKLISTS;
		String[] FIELDS = KEYS_TASKLISTS_TABLE;
		String WHERE = KEY_DELETE_FLAG + "=?";
		String[] WHERE_SELECTION = { String.valueOf(1) };
		Cursor cursor = db.query(TABLE, FIELDS, WHERE, WHERE_SELECTION, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			do {
				listOfTaskList.add(createLocalTaskListObject(cursor));
			} while (cursor.moveToNext());
		}
		return listOfTaskList;
	}

	/**
	 * Returns a task by the external (Google Tasks Api) id.
	 * 
	 * @param googleId
	 *            the Google Tasks Id
	 * @return the task as LocalTask
	 */
	public LocalTask getTaskByGoogleId(String googleId) {
		LocalTask task = null;
		SQLiteDatabase db = this.getReadableDatabase();
		String TABLE = TABLE_TASKS;
		String[] FIELDS = KEYS_TASKS_TABLE;
		String WHERE = KEY_GOOGLE_ID + "=?";
		String[] WHERE_SELECTION = { googleId };
		Cursor cursor = db.query(TABLE, FIELDS, WHERE, WHERE_SELECTION, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			task = createLocalTaskObject(cursor);
		}
		return task;
	}

	/**
	 * Returns a task by the application internal id.
	 * 
	 * @param internalId
	 *            the application internal id (UUID)
	 * @return the task as LocalTask object
	 */
	public LocalTask getTaskByInternalId(String internalId) {
		LocalTask task = null;
		SQLiteDatabase db = this.getReadableDatabase();
		String TABLE = TABLE_TASKS;
		String[] FIELDS = KEYS_TASKS_TABLE;
		String WHERE = KEY_INTERNAL_ID + "=? AND " + KEY_DELETE_FLAG + "=?";
		String[] WHERE_SELECTION = { internalId, String.valueOf(0) };
		Cursor cursor = db.query(TABLE, FIELDS, WHERE, WHERE_SELECTION, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			task = createLocalTaskObject(cursor);
		}
		return task;
	}

	/**
	 * Returns a task list by the external (Google Tasks Api) id.
	 * 
	 * @param googleId
	 *            the Google Tasks Id for the task list
	 * @return the task list as LocalTaskList object
	 */
	public LocalTaskList getTaskListByGoogleId(String googleId) {
		LocalTaskList taskList = null;
		SQLiteDatabase db = this.getReadableDatabase();
		String TABLE = TABLE_TASKLISTS;
		String[] FIELDS = KEYS_TASKLISTS_TABLE;
		String WHERE = KEY_GOOGLE_ID + "=?";
		String[] WHERE_SELECTION = { googleId };
		Cursor cursor = db.query(TABLE, FIELDS, WHERE, WHERE_SELECTION, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			taskList = createLocalTaskListObject(cursor);
		}
		return taskList;
	}

	/**
	 * Returns the task list by the application internal id.
	 * 
	 * @param internalId
	 *            the application internal id (UUID)
	 * @return the task list as LocalTaskList object
	 */
	public LocalTaskList getTaskListByInternalId(String internalId) {
		LocalTaskList taskList = null;
		SQLiteDatabase db = this.getReadableDatabase();
		String TABLE = TABLE_TASKLISTS;
		String[] FIELDS = KEYS_TASKLISTS_TABLE;
		String WHERE = KEY_INTERNAL_ID + "=? AND " + KEY_DELETE_FLAG + "=?";
		String[] WHERE_SELECTION = { internalId, String.valueOf(0) };
		Cursor cursor = db.query(TABLE, FIELDS, WHERE, WHERE_SELECTION, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			taskList = createLocalTaskListObject(cursor);
		}
		return taskList;
	}

	/**
	 * Gets all task lists saved in the database (which are not marked as
	 * deleted).
	 * 
	 * @return all tasklists saved in the database.
	 */
	public List<LocalTaskList> getTaskLists() {
		List<LocalTaskList> listOfTaskList = new ArrayList<LocalTaskList>();
		SQLiteDatabase db = this.getReadableDatabase();
		String TABLE = TABLE_TASKLISTS;
		String[] FIELDS = KEYS_TASKLISTS_TABLE;
		String WHERE = KEY_DELETE_FLAG + "=?";
		String[] WHERE_SELECTION = { String.valueOf(0) };
		Cursor cursor = db.query(TABLE, FIELDS, WHERE, WHERE_SELECTION, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			do {
				listOfTaskList.add(createLocalTaskListObject(cursor));
			} while (cursor.moveToNext());
		}
		return listOfTaskList;
	}

	/**
	 * Gets all task lists without a Google Id (this task lists are yet not
	 * synced).
	 * 
	 * @return all tasklists not synced with google
	 */
	public List<LocalTaskList> getTaskListsWithoutGoogleId() {
		List<LocalTaskList> listOfTaskList = new ArrayList<LocalTaskList>();
		SQLiteDatabase db = this.getReadableDatabase();
		String TABLE = TABLE_TASKLISTS;
		String[] FIELDS = KEYS_TASKLISTS_TABLE;
		String WHERE = KEY_DELETE_FLAG + "=? AND " + KEY_GOOGLE_ID + " IS NULL";
		String[] WHERE_SELECTION = { String.valueOf(0) };
		Cursor cursor = db.query(TABLE, FIELDS, WHERE, WHERE_SELECTION, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			do {
				listOfTaskList.add(createLocalTaskListObject(cursor));
			} while (cursor.moveToNext());
		}
		return listOfTaskList;
	}

	/**
	 * Get all tasks for a given task list
	 * 
	 * @param taskList
	 *            the task list
	 * @return the tasks for this task list
	 */
	public List<LocalTask> getTasks(LocalTaskList taskList) {
		List<LocalTask> listOfTask = new ArrayList<LocalTask>();
		SQLiteDatabase db = this.getReadableDatabase();
		String TABLE = TABLE_TASKS;
		String[] FIELDS = KEYS_TASKS_TABLE;
		String WHERE = KEY_TASK_TASKLIST_ID + "=? AND " + KEY_DELETE_FLAG + "=?";
		String[] WHERE_SELECTION = { taskList.getInternalId(), String.valueOf(0) };
		Cursor cursor = db.query(TABLE, FIELDS, WHERE, WHERE_SELECTION, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			do {
				listOfTask.add(createLocalTaskObject(cursor));
			} while (cursor.moveToNext());
		}
		return listOfTask;
	}

	/**
	 * All tasks without google id (not yet synced with Google Servers).
	 * 
	 * @param taskList
	 *            the task list where the tasks are into
	 * @return all tasks for this task list.
	 */
	public List<LocalTask> getTasksWithoutGoogleId(LocalTaskList taskList) {
		List<LocalTask> listOfTask = new ArrayList<LocalTask>();
		SQLiteDatabase db = this.getReadableDatabase();
		String TABLE = TABLE_TASKS;
		String[] FIELDS = KEYS_TASKS_TABLE;
		String WHERE = KEY_TASK_TASKLIST_ID + "=? AND " + KEY_DELETE_FLAG + "=? AND " + KEY_GOOGLE_ID + " IS NULL";
		String[] WHERE_SELECTION = { taskList.getInternalId(), String.valueOf(0) };
		Cursor cursor = db.query(TABLE, FIELDS, WHERE, WHERE_SELECTION, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			do {
				listOfTask.add(createLocalTaskObject(cursor));
			} while (cursor.moveToNext());
		}
		return listOfTask;
	}

	/**
	 * Creates the tasklist and task table in the SQLite DB.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		createTasklistsTable(db);
		createTasksTable(db);
	}

	/**
	 * If the database version changes the tables will be dropped and recreated.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKLISTS);
		onCreate(db);
	}

	/**
	 * Updates a task in the database.
	 * 
	 * @param task
	 *            the task to be updated
	 * @return 1 if changed
	 */
	public int updateTask(LocalTask task) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = createContentValues(null, task);
		return db.update(TABLE_TASKS, values, KEY_INTERNAL_ID + " = ?", new String[] { task.getInternalId() });
	}

	/**
	 * Updates a task list in the database.
	 * 
	 * @param taskList
	 *            the task list to be updated
	 * @return 1 if changed
	 */
	public int updateTaskList(LocalTaskList taskList) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = createContentValues(taskList);
		return db.update(TABLE_TASKLISTS, values, KEY_INTERNAL_ID + " = ?", new String[] { taskList.getInternalId() });
	}

}
