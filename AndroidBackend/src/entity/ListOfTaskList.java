package entity;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;

public class ListOfTaskList {

	// Attributes
	@ElementList
	private List<TaskList> listOfTaskLists;

	// Constructor
	public ListOfTaskList() {
		listOfTaskLists = new ArrayList<TaskList>();
	}

	// Other Methods
	public void add_TaskList_To_ListOfTaskLists(TaskList taskList) {
		listOfTaskLists.add(taskList);
	}

	public boolean remove_TaskList_of_ListOfTaskLists(TaskList taskList) {
		return listOfTaskLists.remove(taskList);
	}

	public boolean contains_TaskList_in_ListOfTaskLists(TaskList taskList) {
		return listOfTaskLists.contains(taskList);
	}

	public List<TaskList> getListOfTaskList() {
		return this.listOfTaskLists;
	}

}
