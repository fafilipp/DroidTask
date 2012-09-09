package de.htwg.android.taskmanager.backend.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.htwg.android.taskmanager.backend.entity.LocalTask;
import de.htwg.android.taskmanager.backend.entity.LocalTaskList;
import de.htwg.android.taskmanager.backend.util.EStatus;

public class DatabaseHandler extends SQLiteOpenHelper {

	// Database Version and Name
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "HTWG_TaskMngr-DB";

	// Table Name
	private static final String TABLE_TASKLISTS = "htwg_tasklists";
	private static final String TABLE_TASKS = "htwg_tasks";

	// Contacts Table Columns names
	private static final String KEY_INTERNAL_ID = "internal_id";
	private static final String KEY_GOOGLE_ID = "google_id";
	private static final String KEY_LAST_MODIFICATION = "last_modification";
	private static final String KEY_TITLE = "title";
	private static final String KEY_TASK_NOTES = "notes";
	private static final String KEY_TASK_STATUS = "status";
	private static final String KEY_TASK_DUE = "due";
	private static final String KEY_TASK_COMPLETED = "completed";
	private static final String KEY_TASK_TASKLIST_ID = "tasklist_id";

	// Arrays of all columns
	private static final String[] KEYS_TASKLISTS_TABLE = 
			new String[] { KEY_INTERNAL_ID, KEY_GOOGLE_ID, KEY_LAST_MODIFICATION, KEY_TITLE };
	private static final String[] KEYS_TASKS_TABLE = 
			new String[] { KEY_INTERNAL_ID, KEY_GOOGLE_ID, KEY_LAST_MODIFICATION, KEY_TITLE,
			KEY_TASK_NOTES, KEY_TASK_STATUS, KEY_TASK_DUE, KEY_TASK_COMPLETED, KEY_TASK_TASKLIST_ID };

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void addTask(LocalTaskList taskList, LocalTask task) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = createContentValues(taskList, task);
		db.insert(TABLE_TASKS, null, values);
		db.close();
	}

	public void addTaskList(LocalTaskList taskList) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = createContentValues(taskList);
		db.insert(TABLE_TASKLISTS, null, values);
		db.close();
	}

	private ContentValues createContentValues(LocalTaskList taskList) {
		ContentValues values = new ContentValues();
		values.put(KEY_INTERNAL_ID, taskList.getInternalId());
		values.put(KEY_GOOGLE_ID, taskList.getGoogleId());
		values.put(KEY_LAST_MODIFICATION, taskList.getLastModification());
		values.put(KEY_TITLE, taskList.getTitle());
		return values;
	}

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

	private LocalTaskList createTaskListObject(Cursor cursor) {
		LocalTaskList taskList = new LocalTaskList(cursor.getString(cursor.getColumnIndex(KEY_INTERNAL_ID)));
		taskList.setGoogleId(cursor.getString(cursor.getColumnIndex(KEY_GOOGLE_ID)));
		taskList.setLastModification(Long.parseLong(cursor.getString(cursor.getColumnIndex(KEY_LAST_MODIFICATION))));
		taskList.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
		for(LocalTask localTask : getAllTasks(taskList)) {
			taskList.addTaskToList(localTask);
		}
		return taskList;
	}

	private void createTasklistsTable(SQLiteDatabase db) {
		String CREATE_TASKLISTS_TABLE = "CREATE TABLE " + TABLE_TASKLISTS + "(" + KEY_INTERNAL_ID + " TEXT PRIMARY KEY," + KEY_GOOGLE_ID
				+ " TEXT UNIQUE," + KEY_LAST_MODIFICATION + " INTEGER," + KEY_TITLE + " TEXT" + ")";
		db.execSQL(CREATE_TASKLISTS_TABLE);
	}

	private LocalTask createTaskObject(Cursor cursor) {
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

	private void createTasksTable(SQLiteDatabase db) {
		String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "(" + KEY_INTERNAL_ID + " TEXT PRIMARY KEY," + KEY_GOOGLE_ID
				+ " TEXT UNIQUE," + KEY_LAST_MODIFICATION + " INTEGER," + KEY_TITLE + " TEXT," + KEY_TASK_NOTES + " TEXT,"
				+ KEY_TASK_STATUS + " TEXT," + KEY_TASK_DUE + " INTEGER," + KEY_TASK_COMPLETED + " INTEGER," + KEY_TASK_TASKLIST_ID
				+ " INTEGER" + ")";
		db.execSQL(CREATE_TASKS_TABLE);
	}

	private void deleteAllTasksForTaskList(LocalTaskList taskList) {
		SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, KEY_TASK_TASKLIST_ID + " = ?",
                new String[] { String.valueOf(taskList.getInternalId()) });
        db.close();
	}

	public void deleteTask(LocalTask task) {
		SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, KEY_INTERNAL_ID + " = ?",
                new String[] { String.valueOf(task.getInternalId()) });
        db.close();
	}

	public void deleteTaskList(LocalTaskList taskList) {
		deleteAllTasksForTaskList(taskList);
		SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKLISTS, KEY_INTERNAL_ID + " = ?",
                new String[] { String.valueOf(taskList.getInternalId()) });
        db.close();
	}

	public List<LocalTaskList> getAllTaskLists() {
		List<LocalTaskList> listOfTaskList = new ArrayList<LocalTaskList>();
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_TASKLISTS;
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null && cursor.moveToFirst()) {
			do {
				listOfTaskList.add(createTaskListObject(cursor));
			} while (cursor.moveToNext());
		}
		return listOfTaskList;
	}

	public List<LocalTask> getAllTasks(LocalTaskList taskList) {
		List<LocalTask> listOfTask = new ArrayList<LocalTask>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_TASKS, KEYS_TASKS_TABLE, KEY_TASK_TASKLIST_ID + "=?", new String[] { String.valueOf(taskList.getInternalId()) }, null,
				null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			do {
				listOfTask.add(createTaskObject(cursor));
			} while (cursor.moveToNext());
		}
		return listOfTask;
	}

	public LocalTask getTaskByGoogleId(int googleId) {
		LocalTask task = null;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_TASKS, KEYS_TASKS_TABLE, KEY_GOOGLE_ID + "=?", new String[] { String.valueOf(googleId) }, null,
				null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			task = createTaskObject(cursor);
		}
		return task;
	}

	public LocalTask getTaskByInternalId(int internalId) {
		LocalTask task = null;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_TASKS, KEYS_TASKS_TABLE, KEY_INTERNAL_ID + "=?", new String[] { String.valueOf(internalId) }, null,
				null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			task = createTaskObject(cursor);
		}
		return task;
	}

	public LocalTaskList getTaskListByGoogleId(int googleId) {
		LocalTaskList taskList = null;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_TASKLISTS, KEYS_TASKLISTS_TABLE, KEY_GOOGLE_ID + "=?", new String[] { String.valueOf(googleId) },
				null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			taskList = createTaskListObject(cursor);
		}
		return taskList;
	}

	public LocalTaskList getTaskListByInternalId(int internalId) {
		LocalTaskList taskList = null;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_TASKLISTS, KEYS_TASKLISTS_TABLE, KEY_INTERNAL_ID + "=?",
				new String[] { String.valueOf(internalId) }, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			taskList = createTaskListObject(cursor);
		}
		return taskList;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTasklistsTable(db);
		createTasksTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKLISTS);
		onCreate(db);
	}

	public int updateTask(LocalTask task) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = createContentValues(null, task);
		return db.update(TABLE_TASKS, values, KEY_INTERNAL_ID + " = ?", new String[] { task.getInternalId() });
	}

	public int updateTaskList(LocalTaskList taskList) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = createContentValues(taskList);
		return db.update(TABLE_TASKLISTS, values, KEY_INTERNAL_ID + " = ?", new String[] { taskList.getInternalId() });
	}
}
