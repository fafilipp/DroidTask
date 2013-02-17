package de.htwg.android.taskmanager.activity;

import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.ACTIVITY_KEY_EDIT;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.ACTIVITY_KEY_TASK_ID;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.ACTIVITY_KEY_TASK_TITLE;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.REQUEST_CODE_EDIT_ACTIVITY;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.R.drawable;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import de.htwg.android.taskmanager.backend.database.DatabaseHandler;
import de.htwg.android.taskmanager.backend.entity.LocalTask;
import de.htwg.android.taskmanager.backend.util.EStatus;

/**
 * The TaskActivity for this app. This activity shows up the task information as
 * title, notes, completion date on completed task and due date (if given) on
 * not completed tasks. Additionally it allows to edit (call
 * NewAndEditTaskActivity) and delete this task (confirmation dialog and then
 * back to the MainActivity).
 * 
 * @author Filippelli, Gerhart, Gillet
 * 
 */
public class TaskActivity extends Activity {

	/**
	 * The internal id for the showed task.
	 */
	private String taskInternalId;

	/**
	 * The database handler for this activity.
	 */
	private DatabaseHandler dbHandler;

	/**
	 * The local task object for the showed task
	 */
	private LocalTask task;

	/**
	 * Default onActivityResult method of this activity.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null && requestCode == REQUEST_CODE_EDIT_ACTIVITY) {
			data.putExtra(ACTIVITY_KEY_EDIT, true);
			setResult(Activity.RESULT_OK, data);
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * Default onCreate method of this activity.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_task);

		TextView tvTitel = (TextView) findViewById(R.id.title);
		TextView tvNote = (TextView) findViewById(R.id.notes);
		TextView tvDueOrCompletedTitle = (TextView) findViewById(R.id.dueOrCompletedTitle);
		TextView tvDueOrCompleted = (TextView) findViewById(R.id.dueOrCompleted);

		taskInternalId = getIntent().getExtras().getString(ACTIVITY_KEY_TASK_ID);

		dbHandler = new DatabaseHandler(this);
		task = dbHandler.getTaskByInternalId(taskInternalId);
		tvTitel.setText(task.getTitle());
		tvNote.setText(task.getNotes());

		if (task.getStatus().equals(EStatus.COMPLETED)) {
			tvDueOrCompletedTitle.setText("Completion Date");
			tvDueOrCompleted.setText(usingDateFormatter(task.getCompleted()));
		} else {
			if (task.getDue() == 0) {
				tvDueOrCompleted.setText("n/a");
			} else {
				tvDueOrCompleted.setText(usingDateFormatter(task.getDue()));
			}
		}
	}

	/**
	 * Default onCreateOptionsMenu method of this activity.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.task, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Default onOptionsItemSelected method of this activity.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.delete:
			showDeleteDialog(task.getInternalId(), task.getTitle());
			return true;
		case R.id.edit:
			Intent editTaskIntent = new Intent(TaskActivity.this, NewAndEditTaskActivity.class);
			editTaskIntent.putExtra(ACTIVITY_KEY_TASK_ID, taskInternalId);
			editTaskIntent.putExtra(ACTIVITY_KEY_EDIT, true);
			startActivityForResult(editTaskIntent, REQUEST_CODE_EDIT_ACTIVITY);
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * On delete shows up a delete dialog to confirm the deletion, on
	 * confirmation the task will be deleted and the expandable and this
	 * activity finished (go back to the MainActivity).
	 * 
	 * @param internalId
	 *            the internal id of the task
	 * @param title
	 *            the title id of the task
	 */
	private void showDeleteDialog(final String internalId, final String title) {
		new AlertDialog.Builder(this).setTitle("Delete " + title).setIcon(drawable.ic_delete)
				.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dbHandler.deleteTask(internalId);
						Intent resultIntent = new Intent();
						resultIntent.putExtra(ACTIVITY_KEY_EDIT, false);
						resultIntent.putExtra(ACTIVITY_KEY_TASK_TITLE, title);
						setResult(Activity.RESULT_OK, resultIntent);
						finish();
					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();
					}
				}).show();
	}

	/**
	 * Formats a given long - milli seconds date - into a string of the format
	 * "yyyy/MMM/dd hh:mm:ss z"
	 * 
	 * @param input
	 *            the long unix timestamp
	 * @return the date formated in the following format:
	 *         "yyyy/MMM/dd hh:mm:ss z"
	 */
	@SuppressLint("SimpleDateFormat")
	private String usingDateFormatter(long input) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(input);
		return new SimpleDateFormat("dd.MM.yyyy").format(calendar.getTime());
	}
}
