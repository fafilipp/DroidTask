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
import de.htwg.android.taskmanager.google.sync.TaskData;

public class TaskActivity extends Activity {

	private int taskList_pos;
	private int task_pos;

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

		TaskData myData = new TaskData();

		taskList_pos = getIntent().getExtras().getInt("taskList");
		task_pos = getIntent().getExtras().getInt("task");

		DatabaseHandler dbHandler = new DatabaseHandler(this);
		LocalTask task = myData.getAllTasklists(dbHandler).getListOfTaskList()
				.get(taskList_pos).getTaskList().get(task_pos);
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

			break;
		case R.id.edit:

			final Intent editTask_Intent = new Intent(TaskActivity.this,
					de.htwg.android.taskmanager.activity.EditTaskActivity.class);

			editTask_Intent.putExtra("tasklist", taskList_pos);
			editTask_Intent.putExtra("task", task_pos);

			startActivity(editTask_Intent);

			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
