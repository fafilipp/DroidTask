package de.htwg.android.taskmanager.activity;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;
import de.htwg.android.taskmanager.adapter.TaskListAdapter;
import de.htwg.android.taskmanager.backend.binding.MarshallingException;
import de.htwg.android.taskmanager.backend.database.DatabaseHandler;
import de.htwg.android.taskmanager.backend.entity.LocalTaskList;
import de.htwg.android.taskmanager.backend.entity.LocalTaskLists;
import de.htwg.android.taskmanager.google.sync.TaskData;

public class MainActivity extends ExpandableListActivity {

	private TaskListAdapter listAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Änderung commiten!
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final TaskData dummyData = new TaskData();

	DatabaseHandler dbHandler = new DatabaseHandler(this);

			listAdapter = new TaskListAdapter(this, dummyData.getAllTasklists(dbHandler)
					.getListOfTaskList());
			setListAdapter(listAdapter);


			

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
	}
	
	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub

		Intent taskView_Intent = new Intent(this, TaskActivity.class);
		
		taskView_Intent.putExtra("taskList", groupPosition);
		taskView_Intent.putExtra("task", childPosition);
		
		this.startActivity(taskView_Intent);

		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
