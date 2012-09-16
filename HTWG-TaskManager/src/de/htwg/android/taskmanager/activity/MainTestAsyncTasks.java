package de.htwg.android.taskmanager.activity;

import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.GOOGLE_ACCOUNT_TYPE;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.LOG_TAG;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import de.htwg.android.taskmanager.adapter.TaskListAdapter;
import de.htwg.android.taskmanager.backend.database.DatabaseHandler;
import de.htwg.android.taskmanager.backend.entity.LocalTask;
import de.htwg.android.taskmanager.backend.entity.LocalTaskList;
import de.htwg.android.taskmanager.google.task.api.GoogleSyncManager;

public class MainTestAsyncTasks extends ExpandableListActivity {

	private TaskListAdapter listAdapter;
	private List<LocalTaskList> localTaskList;
	
	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		Intent taskView_Intent = new Intent(this, TaskActivity.class);
		LocalTask currentTask = localTaskList.get(groupPosition).getTaskList().get(childPosition);
		taskView_Intent.putExtra("task", currentTask.getInternalId());
		this.startActivity(taskView_Intent);
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		DatabaseHandler dbHandler = new DatabaseHandler(this);
//		LocalTaskList temp = dbHandler.getTaskLists().get(1);
//		LocalTask newTask = new LocalTask();
//		newTask.setTitle("Dummy Title");
//		dbHandler.addTask(temp, newTask);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		AccountManager accountManager = AccountManager.get(this);
		Account[] accounts = accountManager.getAccountsByType(GOOGLE_ACCOUNT_TYPE);
		Log.d(LOG_TAG, "amount of accounts = " + accounts.length);
		if (accounts.length < 1) {
			Log.i(LOG_TAG, "no google accounts found");
		} else {
			Log.d(LOG_TAG, "account name = " + accounts[0].name);
			GoogleSyncManager googleSyncManager = new GoogleSyncManager(this, accounts[0]);
			googleSyncManager.execute();
			try {
				googleSyncManager.get(60, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.d(LOG_TAG, "sync success");
		}

		localTaskList = dbHandler.getTaskLists();
		listAdapter = new TaskListAdapter(this, localTaskList);
		setListAdapter(listAdapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
