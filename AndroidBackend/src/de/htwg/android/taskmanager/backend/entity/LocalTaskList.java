package de.htwg.android.taskmanager.backend.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import de.htwg.android.taskmanager.backend.change.ChangeHistory;
import de.htwg.android.taskmanager.backend.change.DeleteChange;
import de.htwg.android.taskmanager.backend.change.InsertChange;
import de.htwg.android.taskmanager.backend.change.UpdateChange;
import de.htwg.android.taskmanager.backend.util.ETaskListAttributes;

public class LocalTaskList {

	@Element
	private String internalId;
	@Element(required = false)
	private String googleId;
	@Element
	private String title;
	@Element
	private long lastModification;
	@ElementList
	private List<LocalTask> taskList;

	public LocalTaskList() {
		taskList = new ArrayList<LocalTask>();
		this.internalId = java.util.UUID.randomUUID().toString();
	}

	public void addTaskToList(LocalTask task) {
		ChangeHistory.getChangeHistory().offer(new InsertChange(task));
		taskList.add(task);
	}

	public boolean containsTaskInList(LocalTask task) {
		return taskList.contains(task);
	}

	public boolean deleteTaskOfList(LocalTask task) {
		ChangeHistory.getChangeHistory().offer(new DeleteChange(task));
		return taskList.remove(task);
	}

	public String getGoogleId() {
		return googleId;
	}

	public String getInternalId() {
		return internalId;
	}

	public long getLastModification() {
		return lastModification;
	}

	public List<LocalTask> getTaskList() {
		return this.taskList;
	}

	public String getTitle() {
		return title;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}

	public void setTitle(String title) {
		lastModification = new Date().getTime();
		ChangeHistory.getChangeHistory().offer(new UpdateChange(this, ETaskListAttributes.TITLE));
		this.title = title;
	}

}
