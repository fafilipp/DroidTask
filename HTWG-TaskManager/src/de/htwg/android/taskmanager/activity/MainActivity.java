package de.htwg.android.taskmanager.activity;

import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.ACTIVITY_DIALOG_ADD;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.ACTIVITY_DIALOG_CANCEL;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.ACTIVITY_DIALOG_TASK_DELETE;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.ACTIVITY_DIALOG_TASK_EDIT;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.ACTIVITY_KEY_EDIT;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.ACTIVITY_KEY_TASK_ID;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.ACTIVITY_KEY_TASK_TITLE;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.GOOGLE_ACCOUNT_TYPE;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.LOG_TAG;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.REQUEST_CODE_NEW_ACTIVITY;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.REQUEST_CODE_SHOW_ACTIVITY;

import java.util.Set;
import java.util.TreeSet;
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
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.RadioGroup;
import de.htwg.android.taskmanager.adapter.TaskListAdapter;
import de.htwg.android.taskmanager.backend.database.DatabaseHandler;
import de.htwg.android.taskmanager.backend.entity.LocalTask;
import de.htwg.android.taskmanager.backend.entity.LocalTaskList;
import de.htwg.android.taskmanager.google.task.api.GoogleSyncManager;

public class MainActivity extends ExpandableListActivity {

	private DatabaseHandler dbHandler;
	private TaskListAdapter listAdapter;
	private Set<String> expandedTaskLists = new TreeSet<String>();

	/**
	 * A new task list will be created and added to the database, using the
	 * database handler.
	 * 
	 * @param title
	 *            the title for this tasklist
	 */
	private void addNewTaskList(String title) {
		LocalTaskList taskList = new LocalTaskList();
		taskList.setTitle(title);
		dbHandler.addTaskList(taskList);
	}

	/**
	 * While clicking the add button on the main activity an dialog for creating
	 * an object will be opened using this method. Therefore the user will be
	 * asked by an RadioButton if he wants to create a task list or a task. He
	 * is also able to enter a title for the created object. If he creates a
	 * task the NewAndEditTaskActivity will be started, otherwise on the task
	 * list, the title will be set and saved into the database.
	 */
	private void createAddDialog() {
		AlertDialog.Builder addDialog = new AlertDialog.Builder(this);
		LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.add_dialog, null);

		final EditText etTitle = (EditText) view.findViewById(R.id.et_title);
		final RadioGroup rgType = (RadioGroup) view.findViewById(R.id.rg_new_type);

		rgType.check(R.id.rb_task);
		addDialog.setCancelable(true);

		addDialog.setNegativeButton(ACTIVITY_DIALOG_CANCEL, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		addDialog.setPositiveButton(ACTIVITY_DIALOG_ADD, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String newTitle = etTitle.getText().toString();
				if (rgType.getCheckedRadioButtonId() == R.id.rb_task) {
					if (newTitle != null && newTitle.trim().equals("")) {
						startNewAndEditActivity(newTitle, false);
					}
				} else if (rgType.getCheckedRadioButtonId() == R.id.rb_tasklist) {
					if (newTitle != null && !newTitle.trim().equals("")) {
						addNewTaskList(newTitle);
						reloadTaskList();
					}
				}
			}
		});
		addDialog.create();
		addDialog.setView(view);
		addDialog.show();
	}

	/**
	 * TO
	 * 
	 * @param taskList
	 */
	private void createEditTaskListDialog(final LocalTaskList taskList) {
		AlertDialog.Builder addDialog = new AlertDialog.Builder(this);
		LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.edit_dialog, null);

		final EditText etTitle = (EditText) view.findViewById(R.id.et_title);
		etTitle.setText(taskList.getTitle());

		addDialog.setCancelable(true);
		addDialog.setNegativeButton(ACTIVITY_DIALOG_CANCEL, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		addDialog.setPositiveButton(ACTIVITY_DIALOG_ADD, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String newTitle = etTitle.getText().toString();
				if (newTitle != null && !newTitle.trim().equals("")) {
					taskList.modifyTitle(newTitle);
					dbHandler.updateTaskList(taskList);
					reloadTaskList();
				}
			}
		});
		addDialog.create();
		addDialog.setView(view);
		addDialog.show();
	}

	/**
	 * Deletes a task from the database and reloads the task list.
	 * 
	 * @param taskId
	 *            the id of this task.
	 */
	private void deleteTask(String taskId) {
		dbHandler.deleteTask(taskId);
		reloadTaskList();
	}

	/**
	 * Deletes a task list from the database and reloads the task list.
	 * 
	 * @param taskListId
	 *            the id of this task list.
	 */
	private void deleteTaskList(String taskListId) {
		dbHandler.deleteTaskList(taskListId);
		reloadTaskList();
	}

	/**
	 * On the result of an called activity this method will be called.
	 * Afterwards the tasklist will be reloaded into the list adapter.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_NEW_ACTIVITY) {
			reloadTaskList();
		}
		if (requestCode == REQUEST_CODE_SHOW_ACTIVITY) {
			reloadTaskList();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * While clicking on a child in the list adapter the activity TaskActivity
	 * will be started, which shows up some information for this tasks.
	 */
	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		LocalTask task = (LocalTask) listAdapter.getChild(groupPosition, childPosition);
		startShowTaskActivity(task.getInternalId());
		return true;
	}

	/**
	 * Default onContextItemSelected of this activity. It reacts to the clicked
	 * event.
	 */
	public boolean onContextItemSelected(MenuItem menuItem) {
		String clicked = menuItem.getTitle().toString();
		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) menuItem.getMenuInfo();
		int type = ExpandableListView.getPackedPositionType(info.packedPosition);
		int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
		if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
			int childPos = ExpandableListView.getPackedPositionChild(info.packedPosition);
			String taskId = ((LocalTask) listAdapter.getChild(groupPos, childPos)).getInternalId();
			if (clicked.equals(ACTIVITY_DIALOG_TASK_EDIT)) {
				startNewAndEditActivity(taskId, true);
			} else if (clicked.equals(ACTIVITY_DIALOG_TASK_DELETE)) {
				deleteTask(taskId);
			}
			reloadTaskList();
		} else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
			String taskListId = ((LocalTaskList) listAdapter.getGroup(groupPos)).getInternalId();
			if (clicked.equals(ACTIVITY_DIALOG_TASK_EDIT)) {
				createEditTaskListDialog((LocalTaskList) listAdapter.getGroup(groupPos));
			} else if (clicked.equals(ACTIVITY_DIALOG_TASK_DELETE)) {
				deleteTaskList(taskListId);
			}
		}
		reloadTaskList();
		return true;
	}

	/**
	 * Default onCreate Method for this Activity.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dbHandler = new DatabaseHandler(this);
		listAdapter = new TaskListAdapter(this, dbHandler.getTaskLists());
		setListAdapter(listAdapter);
		ExpandableListView list = (ExpandableListView) findViewById(android.R.id.list);
		registerForContextMenu(list);
	}

	/**
	 * Default onCreateContextMenu Method for the on long click listener. If
	 * clicking on task lists then the user is able to edit and delete the task
	 * list. Otherwise while clicking on task the user is able to show, edit and
	 * delete this task.
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) menuInfo;
		int type = ExpandableListView.getPackedPositionType(info.packedPosition);
		int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
		if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
			int childPos = ExpandableListView.getPackedPositionChild(info.packedPosition);
			LocalTask localTask = (LocalTask) listAdapter.getChild(groupPos, childPos);
			menu.setHeaderTitle(localTask.getTitle());
			menu.add(ACTIVITY_DIALOG_TASK_EDIT);
			menu.add(ACTIVITY_DIALOG_TASK_DELETE);
			menu.add(ACTIVITY_DIALOG_CANCEL);
		} else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
			LocalTaskList localTaskList = (LocalTaskList) listAdapter.getGroup(groupPos);
			menu.setHeaderTitle(localTaskList.getTitle());
			menu.add(ACTIVITY_DIALOG_TASK_EDIT);
			menu.add(ACTIVITY_DIALOG_TASK_DELETE);
			menu.add(ACTIVITY_DIALOG_CANCEL);
		}

	}

	/**
	 * Default onCreateOptionsMenu Method for this Activity.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * Removes collapsed group positions from the set. This method is necessary
	 * to save the expanded list state.
	 */
	@Override
	public void onGroupCollapse(int groupPosition) {
		super.onGroupCollapse(groupPosition);
		LocalTaskList localTaskList = (LocalTaskList) listAdapter.getGroup(groupPosition);
		expandedTaskLists.remove(localTaskList.getInternalId());
	}

	/**
	 * Saves all expanded group positions in the set. This method is necessary
	 * to save the expanded list state.
	 */
	@Override
	public void onGroupExpand(int groupPosition) {
		super.onGroupExpand(groupPosition);
		LocalTaskList localTaskList = (LocalTaskList) listAdapter.getGroup(groupPosition);
		expandedTaskLists.add(localTaskList.getInternalId());
	}

	/**
	 * Default onOptionsItemSelected Method for this Activity. Calls
	 * createAddDialog method if clicking on the add button. The sync button
	 * starts the sync with the Google Api using an AsyncTask.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
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

	/**
	 * Reloads the task list in the list adapter. Creates a new TaskListAdapter
	 * and resets it. Afterwards it expands all expanded groups again.
	 */
	private void reloadTaskList() {
		listAdapter = new TaskListAdapter(this, dbHandler.getTaskLists());
		setListAdapter(listAdapter);
		for (String expandedTaskList : expandedTaskLists) {
			int groupToExpand = searchGroupPosForTaskList(expandedTaskList);
			if (groupToExpand != -1) {
				// id found, expand this
				getExpandableListView().expandGroup(groupToExpand);
			} else {
				// remove id from list, because it does't exists anymore.
				expandedTaskLists.remove(expandedTaskList);
			}
		}
	}

	/**
	 * Searchs the group position in the list adapter for an given task list id.
	 * 
	 * @param taskListId
	 *            the internal id of the local task list
	 * @return the group position if the task list is been found in the list
	 *         adapter, -1 if not found.
	 */
	private int searchGroupPosForTaskList(String taskListId) {
		for (int gp = 0; gp < listAdapter.getGroupCount(); gp++) {
			LocalTaskList tmp = (LocalTaskList) listAdapter.getGroup(gp);
			if (tmp.getInternalId().equals(taskListId)) {
				// Group position for this task list found, returning position.
				return gp;
			}
		}
		// Group Position not found, returning -1
		return -1;
	}

	/**
	 * Starts the NewAndEditActivity activity.
	 * 
	 * @param taskIdOrTitle
	 *            if the edit attribute is false then this attribute needs an
	 *            title, otherwise the taskid.
	 * @param edit
	 *            if the activity should create a new task then this attribute
	 *            is false, otherwise for editing tasks is true.
	 */
	private void startNewAndEditActivity(String taskIdOrTitle, boolean edit) {
		Intent editTaskIntent = new Intent(MainActivity.this, NewAndEditTaskActivity.class);
		if (edit) {
			editTaskIntent.putExtra(ACTIVITY_KEY_TASK_ID, taskIdOrTitle);
		} else {
			editTaskIntent.putExtra(ACTIVITY_KEY_EDIT, edit);
		}
		editTaskIntent.putExtra(ACTIVITY_KEY_TASK_TITLE, taskIdOrTitle);
		startActivityForResult(editTaskIntent, REQUEST_CODE_NEW_ACTIVITY);
	}

	/**
	 * Starts the TaskActivity activity.
	 * 
	 * @param taskId
	 *            the internal id of the task which should be showed up.
	 */
	private void startShowTaskActivity(String taskId) {
		Intent taskViewIntent = new Intent(this, TaskActivity.class);
		taskViewIntent.putExtra(ACTIVITY_KEY_TASK_ID, taskId);
		this.startActivityForResult(taskViewIntent, REQUEST_CODE_SHOW_ACTIVITY);
	}

	/**
	 * Starts the Sync with the Google Api. Selects the Google Account and
	 * starts the AsyncTask for syncing tasks with Google.
	 */
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

}
