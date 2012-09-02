package de.htwg.android.taskmanager.backend.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import de.htwg.android.taskmanager.backend.binding.Binding;
import de.htwg.android.taskmanager.backend.binding.MarshallingException;
import de.htwg.android.taskmanager.backend.entity.ListOfTaskList;
import de.htwg.android.taskmanager.backend.entity.Task;
import de.htwg.android.taskmanager.backend.entity.TaskList;
import de.htwg.android.taskmanager.backend.util.EStatus;

public class Main {

	/**
	 * @param args
	 * @throws MarshallingException 
	 */
	public static void main(String[] args) throws MarshallingException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		java.util.Date d = null;
		try {
			d = sdf.parse("2000-12-13T14:00:00Z");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		Task task = new Task();
		task.setLastSynchOnline(new java.sql.Timestamp(d.getTime()));
		task.setLastModification(new java.sql.Timestamp(d.getTime()));
		task.setTitle("Test");
		task.setParentTask("0");
		task.setPosition("0");
		task.setNotes("Keine Notes");
		task.setStatus(EStatus.COMPLETED);
		task.setDue(new java.sql.Timestamp(d.getTime()));
		task.setCompleted(new java.sql.Timestamp(d.getTime()));
		task.setDeleted(false);
		task.setHidden(false);

		Task task1 = new Task();
		task1.setLastSynchOnline(new java.sql.Timestamp(d.getTime()));
		task1.setLastModification(new java.sql.Timestamp(d.getTime()));
		task1.setTitle("Test");
		task1.setParentTask("0");
		task1.setPosition("0");
		task1.setNotes("Keine Notes");
		task1.setStatus(EStatus.COMPLETED);
		task1.setDue(new java.sql.Timestamp(d.getTime()));
		task1.setCompleted(new java.sql.Timestamp(d.getTime()));
		task1.setDeleted(false);
		task1.setHidden(false);

		TaskList taskList = new TaskList();
		taskList.setTitle("TaskList 1");
		taskList.setUpdate(new java.sql.Timestamp(d.getTime()));
		taskList.setDeleted(false);
		taskList.addTaskToList(task);
		taskList.addTaskToList(task1);

		ListOfTaskList lotl = new ListOfTaskList();
		lotl.addTaskList(taskList);

		Binding binding = new Binding();
		binding.marshall(lotl);

		ListOfTaskList lotl_uma = binding.unmarshall();

		for (TaskList tasklist_read : lotl_uma.getListOfTaskList()) {
			System.out.println(tasklist_read.getId());
			for (Task task_read : tasklist_read.getTaskList()) {
				System.out.println(task_read.getId());
			}
		}
	}
}
