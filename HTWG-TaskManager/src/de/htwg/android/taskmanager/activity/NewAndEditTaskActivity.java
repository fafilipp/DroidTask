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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import de.htwg.android.taskmanager.backend.database.DatabaseHandler;
import de.htwg.android.taskmanager.backend.entity.LocalTask;
import de.htwg.android.taskmanager.backend.entity.LocalTaskList;
import de.htwg.android.taskmanager.backend.util.EStatus;

public class NewAndEditTaskActivity extends Activity {

	private String taskInternalId;
	private EditText etTitle;
	private EditText etNote;
	private CheckBox dueDateGiven;
	private DatePicker datePickerDue;
	private Calendar calendar;
	private CheckBox checkBoxCompleted;
	private Spinner spinnerTasklist;
	private LocalTask task;
	private boolean edit;
	private DatabaseHandler dbHandler;
	private List<LocalTaskList> listTaskList;

	/**
	 * This method sets the text given by the user to the task object and calls
	 * updateTask when edit or addTask when add on the database handler.
	 */
	public void addOrUpdateTask() {
		// Get Attributes from View-Objects
		String title = etTitle.getText().toString();
		String note = etNote.getText().toString();
		boolean completed = checkBoxCompleted.isChecked();
		long dueDateTimestamp = getDueDateTimestamp();

		// Check if input data is valid
		if (!validateData(title, note, dueDateTimestamp)) {
			// Some data is invalid, don't save anything.
			return;
		}

		// Save data to DTO and save it to the database
		task.modifyTitle(title);
		task.modifyNotes(note);
		if (completed) {
			task.modifyStatus(EStatus.COMPLETED);
			task.modifyCompleted(Calendar.getInstance().getTimeInMillis());
		} else {
			task.modifyStatus(EStatus.NEEDS_ACTION);
		}
		task.modifyDue(dueDateTimestamp);

		// Save the DTO to the database
		if (edit) {
			dbHandler.updateTask(task);
		} else {
			LocalTaskList localTaskList = listTaskList.get(spinnerTasklist.getSelectedItemPosition());
			dbHandler.addTask(localTaskList, task);
		}

		// Finish Activity, return to previous activity
		finish();
	}

	/**
	 * Returns the date of yesterday in time millis. The date is trimmed to
	 * midnight of the day (day start).
	 * 
	 * @return the today of yesterday in milli seconds
	 */
	private long getDateYesterday() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		long dateToday = calendar.getTimeInMillis();
		return dateToday;
	}

	/**
	 * Gets the Timestamp value for the inputed due date in the View.
	 * 
	 * @return the timestamp value for the due date
	 */
	private long getDueDateTimestamp() {
		int day = datePickerDue.getDayOfMonth();
		int year = datePickerDue.getYear();
		int month = datePickerDue.getMonth();
		calendar.set(year, month, day, 0, 0);
		return calendar.getTimeInMillis();
	}

	/**
	 * Loads the data of an given task when this Activity is in edit mode (not
	 * new task!).
	 */
	public void loadTaskData() {
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
		if (task.getDue() == 0) {
			dueDateGiven.setChecked(false);
		} else {
			calendar.setTimeInMillis(task.getDue());
			dueDateGiven.setChecked(true);
			datePickerDue.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		}
	}

	/**
	 * The default onCreate method of this activity.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		edit = getIntent().getExtras().getBoolean(ACTIVITY_KEY_EDIT);
		if (edit) {
			setContentView(R.layout.edit_task);
		} else {
			setContentView(R.layout.new_task);
		}

		dbHandler = new DatabaseHandler(this);
		calendar = Calendar.getInstance();

		etTitle = (EditText) findViewById(R.id.title);
		etNote = (EditText) findViewById(R.id.notes);
		dueDateGiven = (CheckBox) findViewById(R.id.dueDateGivenCb);
		datePickerDue = (DatePicker) findViewById(R.id.date);
		checkBoxCompleted = (CheckBox) findViewById(R.id.completed);

		if (edit) {
			taskInternalId = getIntent().getExtras().getString(ACTIVITY_KEY_TASK_ID);
			task = dbHandler.getTaskByInternalId(taskInternalId);
			loadTaskData();
		} else {
			task = new LocalTask();
			String title = getIntent().getExtras().getString(ACTIVITY_KEY_TASK_TITLE);
			task.setTitle(title);
			etTitle.setText(title);
			spinnerTasklist = (Spinner) findViewById(R.id.tasklist);
			listTaskList = dbHandler.getTaskLists();
			List<String> list = new ArrayList<String>();
			for (LocalTaskList taskList : listTaskList) {
				list.add(taskList.getTitle());
			}
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerTasklist.setAdapter(dataAdapter);
		}

		dueDateGiven.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					// if timeinmillis == 0, then set calendar to today
					if (calendar.getTimeInMillis() == 0) {
						calendar = Calendar.getInstance();
					}
					datePickerDue.setVisibility(View.VISIBLE);
					datePickerDue.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
				} else {
					Calendar zero = Calendar.getInstance();
					zero.setTimeInMillis(0);
					datePickerDue.updateDate(zero.get(Calendar.YEAR), zero.get(Calendar.MONTH), zero.get(Calendar.DAY_OF_MONTH));
					datePickerDue.setVisibility(View.INVISIBLE);
				}
			}
		});
	}

	/**
	 * Default onCreateOptionsMenu of this Activity
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit_task, menu);
		return true;
	}

	/**
	 * Default onOptionsItemSelected of this Activity. When saving it calls the
	 * addOrUpdateTask method.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.save:
			addOrUpdateTask();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Validates the given properties for no false data.
	 * 
	 * @param title
	 *            the title of the task (validated for not null and not "")
	 * @param note
	 *            the notes for this task (not validated)
	 * @param dueDateTimestamp
	 *            (validated for later then today)
	 * @return true if the input data is valid, otherwise false
	 */
	private boolean validateData(String title, String note, long dueDateTimestamp) {
		if (title == null || title.trim().equals("")) {
			Toast.makeText(this, "No title provided. Please input a title.", Toast.LENGTH_LONG).show();
			return false;
		}
		if (dueDateTimestamp != 0 && dueDateTimestamp < getDateYesterday()) {
			Toast.makeText(this, "The due date is before today. Please provide a valid due date.", Toast.LENGTH_LONG).show();
			return false;
		}
		// if (note == null || note.trim().equals("")) {
		// Toast.makeText(this, "No notes provided. Please input some notes.",
		// Toast.LENGTH_LONG).show();
		// return false;
		// }
		return true;
	}
}
