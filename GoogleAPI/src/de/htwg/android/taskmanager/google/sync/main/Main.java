package de.htwg.android.taskmanager.google.sync.main;

import java.sql.Timestamp;
import java.util.Date;

import com.google.api.services.tasks.model.TaskList;

import de.htwg.android.taskmanager.backend.entity.LocalTask;
import de.htwg.android.taskmanager.backend.util.EStatus;
import de.htwg.android.taskmanager.google.sync.TasksSyncHelper;

public class Main {

	public static void main(String[] args) throws Exception {
		TasksSyncHelper syncManager = new TasksSyncHelper();
		
//		LocalTask t = new LocalTask();
//		t.setTitle("Blubb");
//		t.setStatus(EStatus.NEEDS_ACTION);
//		t.setLastModification(new Timestamp(new Date().getTime()));
//		TaskList tl = syncManager.getAllTasklists().get(0);
//		syncManager.insertNewTask(tl, t);
//		syncManager.createXMLforGoogleData();
		
		syncManager.createXMLforGoogleData();
		
		for(TaskList tl : syncManager.getAllTasklists()) {
			if(tl.getTitle().equals("Test")) {
				System.out.println("deleting tasklist " + tl.getId());
				syncManager.deleteTaskList(tl);
			}
		}
		
//		// Timestamps
//		Timestamp t1 = new Timestamp(2012, 8, 12, 8, 0, 0, 0);
//		Timestamp t2 = new Timestamp(2012, 8, 12, 8, 0, 0, 0);
//		System.out.println(t1.after(null));
	}

}
