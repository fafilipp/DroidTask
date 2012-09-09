package de.htwg.android.taskmanager.google.sync;

import java.util.Date;

import de.htwg.android.taskmanager.backend.database.DatabaseHandler;
import de.htwg.android.taskmanager.backend.entity.LocalTask;
import de.htwg.android.taskmanager.backend.entity.LocalTaskList;
import de.htwg.android.taskmanager.backend.entity.LocalTaskLists;

public class TaskData {

	private void generateDummyIfNoData(DatabaseHandler dbHandler) {
		if (dbHandler.getAllTaskLists().size() == 0) {
			int amountTaskLists = 3;
			int amountTasks = 8;
			for (int i = 0; i < amountTaskLists; i++) {
				LocalTaskList tl = new LocalTaskList();
				tl.setTitle("Tasklist " + i);
				dbHandler.addTaskList(tl);
				for (int j = 0; j < amountTasks; j++) {
					LocalTask t = new LocalTask();
					t.setTitle("Task " + i + " " + j);
					t.setNotes("Hier werden die Notes angezeigt");
					t.setDue(new Date().getTime() + 3600000);
					tl.addTaskToList(t);
					dbHandler.addTask(tl, t);
				}
			}
		}
	}

	public LocalTaskLists getAllTasklists(DatabaseHandler dbHandler) {
		generateDummyIfNoData(dbHandler);
		LocalTaskLists tls = new LocalTaskLists();
		for (LocalTaskList ltaskList : dbHandler.getAllTaskLists()) {
			tls.addTaskList(ltaskList);
		}
		return tls;
	}

}
