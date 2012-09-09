package de.htwg.android.taskmanager.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import de.htwg.android.taskmanager.backend.binding.MarshallingException;
import de.htwg.android.taskmanager.backend.entity.LocalTask;
import de.htwg.android.taskmanager.google.sync.TaskData;

public class TaskActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(findViewById(R.layout.task));
		
		TextView tv_titel = (TextView) findViewById(R.id.title);
		TextView tv_note = (TextView) findViewById(R.id.notes);
		TextView tv_due = (TextView) findViewById(R.id.due);
		TextView tv_complete = (TextView) findViewById(R.id.completed);
		TextView tv_status = (TextView) findViewById(R.id.status);
		
		TaskData myData = new TaskData();
		
		int taskList_pos = (Integer) getIntent().getExtras().get("taskList");
		int task_pos = (Integer) getIntent().getExtras().get("task");
		try {
			LocalTask task = myData.getAllTasklists().getListOfTaskList().get(taskList_pos).getTaskList().get(task_pos);
			tv_titel.setText(task.getTitle());
			tv_note.setText(task.getNotes());
			tv_due.setText(usingDateFormatter(task.getDue()));
			tv_complete.setText(usingDateFormatter(task.getCompleted()));
			tv_status.setText(task.getStatus().toString());
		
		
		} catch (MarshallingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private String usingDateFormatter(long input){  
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
		return true;
	}
}
