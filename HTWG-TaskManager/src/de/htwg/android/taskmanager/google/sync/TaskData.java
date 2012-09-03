package de.htwg.android.taskmanager.google.sync;

import java.util.Date;
import java.util.Iterator;

import com.google.api.services.tasks.model.TaskList;
import com.google.api.services.tasks.model.TaskLists;

import de.htwg.android.taskmanager.backend.binding.Binding;
import de.htwg.android.taskmanager.backend.binding.MarshallingException;
import de.htwg.android.taskmanager.backend.entity.LocalTask;
import de.htwg.android.taskmanager.backend.entity.LocalTaskList;
import de.htwg.android.taskmanager.backend.entity.LocalTaskLists;

public class TaskData {

	public LocalTaskLists getAllTasklists() throws MarshallingException {
		generateDummyData();
		Binding binding = new Binding();
		return binding.unmarshall();
	}

	private LocalTaskLists generateDummyData() throws MarshallingException {
		LocalTaskLists tls = new LocalTaskLists();
		int amountTaskLists = 3;
		int amountTasks = 8;
		for (int i = 0; i < amountTaskLists; i++) {
			LocalTaskList tl = new LocalTaskList();
			tl.setTitle("Tasklist " + i);
			for (int j = 0; j < amountTasks; j++) {
				LocalTask t = new LocalTask();
				t.setTitle("Task " + i + " " + j);
				t.setNotes("Hier werden die Notes angezeigt");
				t.setDue(new Date().getTime() + 3600000);
				tl.addTaskToList(t);
			}
			tls.addTaskList(tl);
		}
		Binding binding = new Binding();
		binding.marshall(tls);
		return tls;
	}

}
