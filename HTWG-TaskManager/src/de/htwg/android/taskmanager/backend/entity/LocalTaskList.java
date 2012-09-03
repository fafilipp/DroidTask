package de.htwg.android.taskmanager.backend.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

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
		taskList.add(task);
	}

	public boolean containsTaskInList(LocalTask task) {
		return taskList.contains(task);
	}

	public boolean deleteTaskOfList(LocalTask task) {
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
		this.title = title;
	}

}
