package de.htwg.android.taskmanager.activity;

import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.ACTIVITY_DIALOG_ADD;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.ACTIVITY_DIALOG_CANCEL;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.ACTIVITY_KEY_EDIT;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.ACTIVITY_KEY_TASK_ID;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.ACTIVITY_KEY_TASK_TITLE;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.GOOGLE_ACCOUNT_TYPE;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.LOG_TAG;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;
import de.htwg.android.taskmanager.adapter.TaskListAdapter;
import de.htwg.android.taskmanager.backend.database.DatabaseHandler;
import de.htwg.android.taskmanager.backend.entity.LocalTask;
import de.htwg.android.taskmanager.backend.entity.LocalTaskList;
import de.htwg.android.taskmanager.google.task.api.GoogleSyncManager;

public class MainActivity extends ExpandableListActivity {

	private DatabaseHandler dbHandler;
	private TaskListAdapter listAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// TODO: avoid this two lines
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		dbHandler = new DatabaseHandler(this);

		listAdapter = new TaskListAdapter(this, dbHandler.getTaskLists());
		setListAdapter(listAdapter);
	}

	private void reloadTaskList() {
		listAdapter = new TaskListAdapter(this, dbHandler.getTaskLists());
		setListAdapter(listAdapter);
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		Intent taskView_Intent = new Intent(this, TaskActivity.class);
		LocalTask task = (LocalTask) listAdapter.getChild(groupPosition, childPosition);
		taskView_Intent.putExtra(ACTIVITY_KEY_TASK_ID, task.getInternalId());
		this.startActivity(taskView_Intent);
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.preferences:
			// TODO: Add preferences activity
			break;
		case R.id.add:
			createAddDialog();
			return true;
		case R.id.sync:
			startSync();
			reloadTaskList();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void startSync() {
		AccountManager accountManager = AccountManager.get(this);
		Account[] accounts = accountManager.getAccountsByType(GOOGLE_ACCOUNT_TYPE);
		Log.d(LOG_TAG, "Amount of Google accounts on device = " + accounts.length);
		if (accounts.length < 1) {
			Log.i(LOG_TAG, "No Google accounts found, no sync possible.");
		} else {
			Log.d(LOG_TAG, "Selected Google account = " + accounts[0].name);
			GoogleSyncManager googleSyncManager = new GoogleSyncManager(this, accounts[0]);
			googleSyncManager.execute();
			try {
				googleSyncManager.get(60, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				Log.d(LOG_TAG, "InterruptedException catched while waiting for Sync response.");
			} catch (ExecutionException e) {
				Log.d(LOG_TAG, "ExecutionException catched while waiting for Sync response.");
			} catch (TimeoutException e) {
				Log.d(LOG_TAG, "TimeoutException catched while waiting for Sync response.");
			}
		}
	}

	private void createAddDialog() {
		AlertDialog.Builder addDialog = new AlertDialog.Builder(this);
		LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.add_dialog, null);
		final RadioGroup rg_type = (RadioGroup) view.findViewById(R.id.rg_new_type);
		final EditText et_title = (EditText) view.findViewById(R.id.et_title);

		rg_type.check(R.id.rb_task);
		addDialog.setCancelable(true);

		addDialog.setNegativeButton(ACTIVITY_DIALOG_CANCEL, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		addDialog.setPositiveButton(ACTIVITY_DIALOG_ADD, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (rg_type.getCheckedRadioButtonId() == R.id.rb_task) {
					Intent editTask_Intent = new Intent(MainActivity.this, NewAndEditTaskActivity.class);
					editTask_Intent.putExtra(ACTIVITY_KEY_EDIT, false);
					editTask_Intent.putExtra(ACTIVITY_KEY_TASK_TITLE, et_title.getText().toString());
					startActivity(editTask_Intent);
				} else if (rg_type.getCheckedRadioButtonId() == R.id.rb_tasklist) {
					addNewTaskList(et_title.getText().toString());
					reloadTaskList();
				}
			}
		});
		addDialog.create();
		addDialog.setView(view);
		addDialog.show();
	}

	private void addNewTaskList(String title) {
		LocalTaskList taskList = new LocalTaskList();
		taskList.setTitle(title);
		dbHandler.addTaskList(taskList);
	}

}
