package de.htwg.android.taskmanager.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import de.htwg.android.taskmanager.adapter.TaskListAdapter;
import de.htwg.android.taskmanager.backend.database.DatabaseHandler;
import de.htwg.android.taskmanager.backend.entity.LocalTask;
import de.htwg.android.taskmanager.backend.entity.LocalTaskList;

public class MainActivity extends ExpandableListActivity {

	private TaskListAdapter listAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Änderung commiten!
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		DatabaseHandler dbHandler = new DatabaseHandler(this);

		listAdapter = new TaskListAdapter(this, dbHandler.getTaskLists());
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

		LocalTask task = (LocalTask) listAdapter.getChild(groupPosition,
				childPosition);

		taskView_Intent.putExtra("task_id", task.getInternalId());

		this.startActivity(taskView_Intent);

		return false;
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

			break;
		case R.id.add:
			addDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void addDialog() {
		final AlertDialog.Builder addDialog = new AlertDialog.Builder(this);
		LayoutInflater layoutInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view = layoutInflater.inflate(R.layout.add_dialog, null);
		final RadioGroup rg_type = (RadioGroup) view
				.findViewById(R.id.rg_new_type);
		final EditText et_title = (EditText) view.findViewById(R.id.et_title);
	
		addDialog.setCancelable(true);
		addDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});

		
		addDialog.setPositiveButton("Add",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						if (rg_type.getCheckedRadioButtonId() == 0) {
							final Intent editTask_Intent = new Intent(
									MainActivity.this,
									de.htwg.android.taskmanager.activity.NewAndEditTaskActivity.class);

							editTask_Intent.putExtra("edit", false);
							editTask_Intent.putExtra("title", et_title.getText().toString());

							startActivity(editTask_Intent);
						} else {

							DatabaseHandler dbHandler = new DatabaseHandler(
									MainActivity.this);

							LocalTaskList taskList = new LocalTaskList();
							taskList.setTitle(et_title.getText().toString());

							dbHandler.addTaskList(taskList);

						}

					}
				});

		addDialog.create();
		addDialog.setView(view);
		addDialog.show();
	}

}
