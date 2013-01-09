package de.htwg.android.taskmanager.activity;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import de.htwg.android.taskmanager.backend.database.DatabaseHandler;
import de.htwg.android.taskmanager.backend.entity.LocalTask;
import de.htwg.android.taskmanager.backend.entity.LocalTaskList;

public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	public MainActivityTest() {
		super(MainActivity.class);
	}

	public void testActivityTestCaseSetUpProperly() {
		assertNotNull("activity should be launched successfully", getActivity());
		
		
		
	}
	
	public void testTaskActivity(){
		DatabaseHandler db = new DatabaseHandler(getActivity());
		LocalTaskList localTaskList = new LocalTaskList();
		localTaskList.setTitle("TestTaskList");
		LocalTask localTask = new LocalTask();
		localTask.setTitle("TestTask");
		db.addTaskList(localTaskList);
		db.addTask(localTaskList, localTask);
		
		Intent intent = new Intent(getActivity(), TaskActivity.class);
		intent.putExtra("task-id", localTask.getInternalId());
		setActivityIntent(intent);
	}
	
	public void testNewAndEditTaskActivity(){
		
		DatabaseHandler db = new DatabaseHandler(getActivity());
		LocalTaskList localTaskList = new LocalTaskList();
		localTaskList.setTitle("TestTaskList");
		LocalTask localTask = new LocalTask();
		localTask.setTitle("TestTask");
		db.addTaskList(localTaskList);
		db.addTask(localTaskList, localTask);
		
		Intent intent = new Intent(getActivity(), NewAndEditTaskActivity.class);
		intent.putExtra("task-id", localTask.getInternalId());
		setActivityIntent(intent);
	}
	

}