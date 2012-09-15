package de.htwg.android.taskmanager.backend.entity;

import java.util.ArrayList;
import java.util.List;

public class LocalTaskLists {

	// Attributes
	private List<LocalTaskList> listOfTaskLists;
	private long lastSuccessfulSync;

	// Other Methods
	public void addTaskList(LocalTaskList taskList) {
		getListOfTaskList().add(taskList);
	}

	public boolean containsTaskList(LocalTaskList taskList) {
		return getListOfTaskList().contains(taskList);
	}

	public long getLastSuccessfulSync() {
		return lastSuccessfulSync;
	}

	public List<LocalTaskList> getListOfTaskList() {
		if (this.listOfTaskLists == null) {
			listOfTaskLists = new ArrayList<LocalTaskList>();
		}
		return this.listOfTaskLists;
	}

	public boolean removeTaskList(LocalTaskList taskList) {
		return getListOfTaskList().remove(taskList);
	}

	public void setLastSuccessfulSync(long lastSuccessfulSync) {
		this.lastSuccessfulSync = lastSuccessfulSync;
	}
}
