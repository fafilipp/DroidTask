package main;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import xml.Marshalling;
import xml.Unmarshalling;
import entity.EStatus;
import entity.ListOfTaskList;
import entity.Task;
import entity.TaskList;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss'Z'" );
		java.util.Date d = null;
		try {
			d = sdf.parse("2000-12-13T14:00:00Z");
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Task task = new Task();
		task.setLastSynchOnline(new java.sql.Timestamp(d.getTime()));
		task.setLastModification("2010-10-15T12:00:00.000Z");
		task.setTitle("Test");
		task.setParentTask("0");
		task.setPosition("0");
		task.setNotes("Keine Notes");
		task.setStatus(EStatus.completed);
		task.setDue(new java.sql.Timestamp(d.getTime()));
		task.setCompleted(new java.sql.Timestamp(d.getTime()));
		task.setDeleted(false);
		task.setHidden(false);
		
		Task task1 = new Task();
		task1.setLastSynchOnline(new java.sql.Timestamp(d.getTime()));
		task1.setLastModification("2010-10-15T12:00:00.000Z");
		task1.setTitle("Test");
		task1.setParentTask("0");
		task1.setPosition("0");
		task1.setNotes("Keine Notes");
		task1.setStatus(EStatus.completed);
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
		lotl.add_TaskList_To_ListOfTaskLists(taskList);
		
		Marshalling ma = new Marshalling();
		ma.SaveToXML(lotl);
		
		Unmarshalling uma = new Unmarshalling();
		ListOfTaskList lotl_uma = uma.getFromXML();
		
		for (TaskList tasklist_read : lotl_uma.getListOfTaskList()) {
			System.out.println(tasklist_read.getID());
			for(Task task_read : tasklist_read.getTaskList()) {
				System.out.println(task_read.getId());
			}
		}
	}
}
