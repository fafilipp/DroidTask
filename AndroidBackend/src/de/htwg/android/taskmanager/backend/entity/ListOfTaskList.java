package de.htwg.android.taskmanager.backend.entity;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;

public class ListOfTaskList {

	// Attributes
	@ElementList
	private List<TaskList> listOfTaskLists;

	// Other Methods
	public void addTaskList(TaskList taskList) {
		getListOfTaskList().add(taskList);
	}

	public boolean containsTaskList(TaskList taskList) {
		return getListOfTaskList().contains(taskList);
	}

	public List<TaskList> getListOfTaskList() {
		if(this.listOfTaskLists == null) {
			listOfTaskLists = new ArrayList<TaskList>();
		}
		return this.listOfTaskLists;
	}

	public boolean removeTaskList(TaskList taskList) {
		return getListOfTaskList().remove(taskList);
	}

}
