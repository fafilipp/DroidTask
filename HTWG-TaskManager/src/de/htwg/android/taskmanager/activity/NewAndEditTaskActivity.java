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

	private String taskInternalId;
	private EditText etTitle;
	private EditText etNote;
	private DatePicker datePickerDue;
	private TimePicker timePickerDue;
	private Calendar calendar;
	private CheckBox checkBoxCompleted;
	private Spinner spinnerTasklist;
	private LocalTask task;
	private boolean edit;
	private DatabaseHandler dbHandler;
	private List<LocalTaskList> listTaskList;

	public void addOrUpdateTask() {
		String title = etTitle.getText().toString();
		if (title.equals("")) {
			Toast.makeText(this, "Title is empty! Add or Update not possible.", Toast.LENGTH_LONG).show();
			return;
		}
		String note = etNote.getText().toString();
		boolean completed = checkBoxCompleted.isChecked();
		int day = datePickerDue.getDayOfMonth();
		int year = datePickerDue.getYear();
		int month = datePickerDue.getMonth();
		int hour = timePickerDue.getCurrentHour();
		int minute = timePickerDue.getCurrentMinute();

		task.modifyTitle(title);
		task.modifyNotes(note);
		if (completed) {
			task.modifyStatus(EStatus.COMPLETED);
			task.modifyCompleted(Calendar.getInstance().getTimeInMillis());
		} else {
			task.modifyStatus(EStatus.NEEDS_ACTION);
		}
		calendar.set(year, month, day, hour, minute);
		long timestamp = calendar.getTimeInMillis();
		task.modifyDue(timestamp);

		if (edit) {
			dbHandler.updateTask(task);
		} else {
			LocalTaskList localTaskList = listTaskList.get(spinnerTasklist.getSelectedItemPosition());
			dbHandler.addTask(localTaskList, task);
		}
		
		// finish acitivity
		finish();
	}

	public void loadTaskData() {
		calendar.setTimeInMillis(task.getDue());
		etTitle.setText(task.getTitle());
		etNote.setText(task.getNotes());
		switch (task.getStatus()) {
		case NEEDS_ACTION:
			checkBoxCompleted.setChecked(false);
			break;
		case COMPLETED:
			checkBoxCompleted.setChecked(true);
			break;
		}
		datePickerDue.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		timePickerDue.setCurrentHour(calendar.get(Calendar.HOUR));
		timePickerDue.setCurrentMinute(calendar.get(Calendar.MINUTE));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_and_edit_task);

		edit = getIntent().getExtras().getBoolean(ACTIVITY_KEY_EDIT);

		dbHandler = new DatabaseHandler(this);
		calendar = Calendar.getInstance();

		etTitle = (EditText) findViewById(R.id.title);
		etNote = (EditText) findViewById(R.id.notes);
		datePickerDue = (DatePicker) findViewById(R.id.date);
		timePickerDue = (TimePicker) findViewById(R.id.time);
		checkBoxCompleted = (CheckBox) findViewById(R.id.completed);
		spinnerTasklist = (Spinner) findViewById(R.id.tasklist);

		if (edit) {
			taskInternalId = getIntent().getExtras().getString(ACTIVITY_KEY_TASK_ID);
			task = dbHandler.getTaskByInternalId(taskInternalId);
			loadTaskData();
		} else {
			task = new LocalTask();
			String title = getIntent().getExtras().getString(ACTIVITY_KEY_TASK_TITLE);
			task.setTitle(title);
			etTitle.setText(title);

			TextView tvTaskList = (TextView) findViewById(R.id.lb_tasklist);
			tvTaskList.setVisibility(View.VISIBLE);
			spinnerTasklist.setVisibility(View.VISIBLE);

			listTaskList = dbHandler.getTaskLists();
			List<String> list = new ArrayList<String>();
			for (LocalTaskList taskList : listTaskList) {
				list.add(taskList.getTitle());
			}
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerTasklist.setAdapter(dataAdapter);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit_task, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.save:
			addOrUpdateTask();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
