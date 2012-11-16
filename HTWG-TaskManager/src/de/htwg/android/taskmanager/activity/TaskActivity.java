package de.htwg.android.taskmanager.activity;

import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.ACTIVITY_KEY_EDIT;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.ACTIVITY_KEY_TASK_ID;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.REQUEST_CODE_EDIT_ACTIVITY;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import de.htwg.android.taskmanager.backend.database.DatabaseHandler;
import de.htwg.android.taskmanager.backend.entity.LocalTask;
import de.htwg.android.taskmanager.backend.util.EStatus;

public class TaskActivity extends Activity {

	private String taskInternalId;
	private DatabaseHandler dbHandler;
	private LocalTask task;

	/**
	 * Default onActivityResult method of this activity.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_EDIT_ACTIVITY) {
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
		// CheckBox tvStatus = (CheckBox) findViewById(R.id.status);

		taskInternalId = getIntent().getExtras().getString(ACTIVITY_KEY_TASK_ID);

		dbHandler = new DatabaseHandler(this);
		task = dbHandler.getTaskByInternalId(taskInternalId);
		tvTitel.setText(task.getTitle());
		tvNote.setText(task.getNotes());

		if (task.getStatus().equals(EStatus.COMPLETED)) {
			// tvStatus.setChecked(true);
			tvDueOrCompletedTitle.setText("Completion Date");
			tvDueOrCompleted.setText(usingDateFormatter(task.getCompleted()));
		} else {
			// tvStatus.setChecked(false);
			tvDueOrCompleted.setText(usingDateFormatter(task.getDue()));
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
			dbHandler.deleteTask(task.getInternalId());
			finish();
			return true;
		case R.id.edit:
			Intent editTaskIntent = new Intent(TaskActivity.this, NewAndEditTaskActivity.class);
			editTaskIntent.putExtra(ACTIVITY_KEY_TASK_ID, taskInternalId);
			editTaskIntent.putExtra(ACTIVITY_KEY_EDIT, true);
			startActivityForResult(editTaskIntent, REQUEST_CODE_EDIT_ACTIVITY);
			return true;
		}
		return super.onOptionsItemSelected(item);
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
	private String usingDateFormatter(long input) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(input);
		return new SimpleDateFormat("dd.MM.yyyy").format(calendar.getTime());
	}
}
