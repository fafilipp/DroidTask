package de.htwg.android.taskmanager.backend.entity;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import de.htwg.android.taskmanager.backend.change.ChangeHistory;
import de.htwg.android.taskmanager.backend.change.DeleteChange;
import de.htwg.android.taskmanager.backend.change.InsertChange;

public class LocalTaskLists {

	// Attributes
	@ElementList
	private List<LocalTaskList> listOfTaskLists;

	@Element(required = false)
	private long lastSuccessfulSync;

	// Other Methods
	public void addTaskList(LocalTaskList taskList) {
		ChangeHistory.getChangeHistory().offer(new InsertChange(taskList));
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
		ChangeHistory.getChangeHistory().offer(new DeleteChange(taskList));
		return getListOfTaskList().remove(taskList);
	}

	public void setLastSuccessfulSync(long lastSuccessfulSync) {
		this.lastSuccessfulSync = lastSuccessfulSync;
	}
}
