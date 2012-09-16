package de.htwg.android.taskmanager.activity;

import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.ACTIVITY_KEY_EDIT;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.ACTIVITY_KEY_TASK_ID;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.ACTIVITY_KEY_TASK_TITLE;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import de.htwg.android.taskmanager.backend.database.DatabaseHandler;
import de.htwg.android.taskmanager.backend.entity.LocalTask;
import de.htwg.android.taskmanager.backend.entity.LocalTaskList;
import de.htwg.android.taskmanager.backend.util.EStatus;

public class NewAndEditTaskActivity extends Activity {

	private String task_id;
	private EditText et_title;
	private EditText et_note;
	private DatePicker dp_due;
	private TimePicker tp_due;
	private Calendar calendar;
	private CheckBox cb_completed;
	private Spinner sp_tasklist;
	private LocalTask task;
	private boolean edit;
	private DatabaseHandler dbHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_and_edit_task);

		edit = getIntent().getExtras().getBoolean(ACTIVITY_KEY_EDIT);

		dbHandler = new DatabaseHandler(this);
		calendar = Calendar.getInstance();

		et_title = (EditText) findViewById(R.id.title);
		et_note = (EditText) findViewById(R.id.notes);
		dp_due = (DatePicker) findViewById(R.id.date);
		tp_due = (TimePicker) findViewById(R.id.time);
		cb_completed = (CheckBox) findViewById(R.id.completed);
		sp_tasklist = (Spinner) findViewById(R.id.tasklist);

		if (edit) {
			task_id = getIntent().getExtras().getString(ACTIVITY_KEY_TASK_ID);
			task = dbHandler.getTaskByInternalId(task_id);
			loadTaskData();
		} else {
			String title = getIntent().getExtras().getString(ACTIVITY_KEY_TASK_TITLE);
			et_title.setText(title);

			sp_tasklist.setVisibility(View.VISIBLE);
			TextView lb_taskList = (TextView) findViewById(R.id.lb_tasklist);

			lb_taskList.setVisibility(View.VISIBLE);

			List<LocalTaskList> listTaskList = dbHandler.getTaskLists();;
			List<String> list = new ArrayList<String>();
			for (LocalTaskList taskList : listTaskList) {
				list.add(taskList.getTitle());
			}

			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, list);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp_tasklist.setAdapter(dataAdapter);

		}

		setDate();

	}

	public void loadTaskData() {
		calendar.setTimeInMillis(task.getDue());
		et_title.setText(task.getTitle());
		et_note.setText(task.getNotes());
	}

	public void setDate() {
		dp_due.updateDate(calendar.get(calendar.YEAR),
				calendar.get(calendar.MONTH),
				calendar.get(calendar.DAY_OF_MONTH));
		tp_due.setCurrentHour(calendar.get(calendar.HOUR));
		tp_due.setCurrentMinute(calendar.get(calendar.MINUTE));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit_task, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add:

			saveOrUpdateTask();

			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void saveOrUpdateTask() {
		String title = et_title.getText().toString();
		if (title.equals("")) {
			Toast.makeText(this,
					"Title is empty! Update or Save not possible.",
					Toast.LENGTH_LONG);
			return;
		}

		String note = et_note.getText().toString();

		int day = dp_due.getDayOfMonth();
		int year = dp_due.getYear();
		int month = dp_due.getMonth();
		int hour = tp_due.getCurrentHour();
		int minute = tp_due.getCurrentMinute();

		calendar.set(year, month, day, hour, minute);
		long timestamp = calendar.getTimeInMillis();

		if (cb_completed.isChecked()) {
			task.setStatus(EStatus.COMPLETED);
			task.setCompleted(Calendar.getInstance().getTimeInMillis());
		} else {
			task.setStatus(EStatus.NEEDS_ACTION);
		}

		task.setDue(timestamp);
		task.setNotes(note);
		task.setTitle(title);

		// TODO: add or update database
		if (!edit) {
			String taskList = (String) sp_tasklist.getSelectedItem();
		}

	}
}
