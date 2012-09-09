package de.htwg.android.taskmanager.activity;

import de.htwg.android.taskmanager.backend.database.DatabaseHandler;
import de.htwg.android.taskmanager.backend.entity.LocalTaskList;
import de.htwg.android.taskmanager.google.sync.TaskData;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// StrictMode.ThreadPolicy policy = new
		// StrictMode.ThreadPolicy.Builder().permitAll().build();
		// StrictMode.setThreadPolicy(policy);
		// AccountManager accountManager = AccountManager.get(this);
		// Account[] accounts =
		// accountManager.getAccountsByType(GOOGLE_ACCOUNT_TYPE);
		// Log.d(LOG_TAG, "amount of accounts = " + accounts.length);
		// if (accounts.length < 1) {
		// Log.i(LOG_TAG, "no google accounts found");
		// } else {
		// Log.d(LOG_TAG, "account name = " + accounts[0].name);
		// try {
		// IGoogleTaskManager taskManager = new AsyncTasksManager(this,
		// accounts[0]);
		// for (TaskList taskList : taskManager.getTaskLists()) {
		// Log.i(LOG_TAG, taskList.getTitle());
		// }
		// } catch (GoogleSyncException e) {
		// e.printStackTrace();
		// }
		// }

		// try {
		// DatabaseHandler db = new DatabaseHandler(this);
		// TaskData td = new TaskData();
		// Log.i(LOG_TAG, "Start saving data to the database");
		// for (LocalTaskList tl : td.getAllTasklists().getListOfTaskList()) {
		// db.addTaskList(tl);
		// Log.i(LOG_TAG, tl.getTitle());
		// }
		// Log.i(LOG_TAG, "Data saved to the database");
		//
		// Log.i(LOG_TAG, "Retrieving data from the database");
		// for (LocalTaskList tl : db.getAllTaskLists()) {
		// Log.i(LOG_TAG, tl.getInternalId() + " - " + tl.getTitle());
		// }
		// Log.i(LOG_TAG, "Retrieving data from the database finished");
		//
		// Log.i(LOG_TAG, "Updating title of first tasklist");
		// LocalTaskList ltl = db.getAllTaskLists().get(0);
		// ltl.setTitle("New Tasklist title");
		// db.updateTaskList(ltl);
		// Log.i(LOG_TAG, "Updating title of first tasklist finished");
		//
		// Log.i(LOG_TAG,
		// "Retrieving data from the database and deleting them");
		// for (LocalTaskList tl : db.getAllTaskLists()) {
		// Log.i(LOG_TAG, tl.getInternalId() + " - " + tl.getTitle());
		// db.deleteTaskList(tl);
		// }
		// Log.i(LOG_TAG,
		// "Retrieving data from the database and deleting them finished");
		//
		// Log.i(LOG_TAG, "Retrieving data from the database");
		// for (LocalTaskList tl : db.getAllTaskLists()) {
		// Log.i(LOG_TAG, tl.getInternalId() + " - " + tl.getTitle());
		// }
		// Log.i(LOG_TAG, "Retrieving data from the database finished");
		//
		// } catch (Exception e) {
		//
		// }

		DatabaseHandler dbHandler = new DatabaseHandler(this);
		TaskData taskData = new TaskData();
		for(LocalTaskList ltl : taskData.getAllTasklists(dbHandler).getListOfTaskList()) {
			System.out.println(ltl.getInternalId() + " - " + ltl.getTitle());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
