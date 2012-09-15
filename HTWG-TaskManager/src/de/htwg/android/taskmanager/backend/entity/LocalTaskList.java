package de.htwg.android.taskmanager.backend.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocalTaskList {

	private String internalId;
	private String googleId;
	private String title;
	private long lastModification;
	private List<LocalTask> taskList;

	public LocalTaskList() {
		this(java.util.UUID.randomUUID().toString());
	}

	public LocalTaskList(String internalId) {
		taskList = new ArrayList<LocalTask>();
		this.internalId = internalId;
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LocalTaskList other = (LocalTaskList) obj;
		if (internalId == null) {
			if (other.internalId != null)
				return false;
		} else if (!internalId.equals(other.internalId))
			return false;
		return true;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((internalId == null) ? 0 : internalId.hashCode());
		return result;
	}

	public void modifyTitle(String title) {
		lastModification = new Date().getTime();
		this.title = title;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}
	
	public void setLastModification(long lastModification) {
		this.lastModification = lastModification;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

}
