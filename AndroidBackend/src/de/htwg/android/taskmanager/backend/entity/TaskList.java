package de.htwg.android.taskmanager.backend.entity;

import static de.htwg.android.taskmanager.backend.util.Util.GOOGLE_DATE_FORMAT;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

public class TaskList {

	// Attributes
	@Element
	private String id;
	@Element
	private String title;
	@Element
	private String update;
	@Element(required = false)
	private String lastSynchOnline;
	@Element
	private boolean deleted;
	@ElementList
	private List<Task> taskList;

	// Constructor
	public TaskList() {
		taskList = new ArrayList<Task>();
		this.setId(java.util.UUID.randomUUID().toString());
	}

	// Other Methodes
	public void addTaskToList(Task task) {
		taskList.add(task);
	}

	public boolean containsTaskInList(Task task) {
		return taskList.contains(task);
	}

	public boolean deleteTaskOfList(Task task) {
		return taskList.remove(task);
	}

	// Getter & Setter
	public String getId() {
		return id;
	}

	public Timestamp getLastSynchOnline() {
		if (this.lastSynchOnline == null) {
			return null;
		}
		return java.sql.Timestamp.valueOf(this.lastSynchOnline);
	}

	public List<Task> getTaskList() {
		return this.taskList;
	}

	public String getTitle() {
		return title;
	}

	public Timestamp getUpdate() {
		return java.sql.Timestamp.valueOf(this.update);
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLastSynchOnline(Timestamp lastSynchOnline) {
		if (lastSynchOnline != null) {
			this.lastSynchOnline = new SimpleDateFormat(GOOGLE_DATE_FORMAT).format(lastSynchOnline);
		} else {
			this.lastSynchOnline = null;
		}
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUpdate(Timestamp update) {
		this.update = new SimpleDateFormat(GOOGLE_DATE_FORMAT).format(update);
	}
}
