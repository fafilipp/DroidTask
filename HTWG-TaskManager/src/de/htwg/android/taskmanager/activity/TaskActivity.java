package de.htwg.android.taskmanager.activity;

import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.ACTIVITY_KEY_EDIT;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.ACTIVITY_KEY_TASK_ID;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.REQUEST_CODE_EDIT_ACTIVITY;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import de.htwg.android.taskmanager.backend.database.DatabaseHandler;
import de.htwg.android.taskmanager.backend.entity.LocalTask;
public class TaskActivity extends Activity {

	private String taskInternalId;
	private DatabaseHandler dbHandler;
	private LocalTask task;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_CODE_EDIT_ACTIVITY) {
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task);

		TextView tvTitel = (TextView) findViewById(R.id.title);
		TextView tvNote = (TextView) findViewById(R.id.notes);
		TextView tvDue = (TextView) findViewById(R.id.due);
		TextView tvComplete = (TextView) findViewById(R.id.completed);
		TextView tvStatus = (TextView) findViewById(R.id.status);

		taskInternalId = getIntent().getExtras().getString(ACTIVITY_KEY_TASK_ID);

		dbHandler = new DatabaseHandler(this);
		task = dbHandler.getTaskByInternalId(taskInternalId);
		tvTitel.setText(task.getTitle());
		tvNote.setText(task.getNotes());
		tvDue.setText(usingDateFormatter(task.getDue()));
		tvComplete.setText(usingDateFormatter(task.getCompleted()));
		tvStatus.setText(task.getStatus().toString());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.task, menu);
		return super.onCreateOptionsMenu(menu);
	}

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
	
	private String usingDateFormatter(long input) {
		Date date = new Date(input);
		Calendar calendar = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MMM/dd hh:mm:ss z");
		sdf.setCalendar(calendar);
		calendar.setTime(date);
		return sdf.format(date);

	}
}

