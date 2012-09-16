package de.htwg.android.taskmanager.activity;

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

	private String task_id;
	private DatabaseHandler dbHandler;
	private LocalTask task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task);

		TextView tv_titel = (TextView) findViewById(R.id.title);
		TextView tv_note = (TextView) findViewById(R.id.notes);
		TextView tv_due = (TextView) findViewById(R.id.due);
		TextView tv_complete = (TextView) findViewById(R.id.completed);
		TextView tv_status = (TextView) findViewById(R.id.status);

		task_id = getIntent().getExtras().getString("task_id");

		dbHandler = new DatabaseHandler(this);
		task = dbHandler.getTaskByInternalId(task_id);
		tv_titel.setText(task.getTitle());
		tv_note.setText(task.getNotes());
		tv_due.setText(usingDateFormatter(task.getDue()));
		tv_complete.setText(usingDateFormatter(task.getCompleted()));
		tv_status.setText(task.getStatus().toString());

	}

	private String usingDateFormatter(long input) {
		Date date = new Date(input);
		Calendar cal = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MMM/dd hh:mm:ss z");
		sdf.setCalendar(cal);
		cal.setTime(date);
		return sdf.format(date);

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
			break;
		case R.id.edit:

			final Intent editTask_Intent = new Intent(
					TaskActivity.this,
					de.htwg.android.taskmanager.activity.NewAndEditTaskActivity.class);

			editTask_Intent.putExtra("task_id", task_id);
			editTask_Intent.putExtra("edit", true);

			startActivity(editTask_Intent);

			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
