package de.htwg.android.taskmanager.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import de.htwg.android.taskmanager.backend.database.DatabaseHandler;
import de.htwg.android.taskmanager.backend.entity.LocalTask;
import de.htwg.android.taskmanager.google.sync.TaskData;

public class EditTaskActivity extends Activity {
		
	
	private int taskList_pos;
	private int task_pos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_task);
		
		TaskData myData = new TaskData();

		taskList_pos = (Integer) getIntent().getExtras().getInt("taskList");
		task_pos = (Integer) getIntent().getExtras().getInt("task");
		
		DatabaseHandler dbHandler = new DatabaseHandler(this);
		LocalTask task = myData.getAllTasklists(dbHandler).getListOfTaskList()
				.get(taskList_pos).getTaskList().get(task_pos);
		
		
		EditText et_titel = (EditText) findViewById(R.id.title);
		EditText et_note = (EditText) findViewById(R.id.notes);
		
		
		et_titel.setText(task.getTitle());
		et_note.setText(task.getNotes());
		
		
	}
}
