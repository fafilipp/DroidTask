package entity;

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
	@Element
	private boolean deleted;
	@ElementList
	private List<Task> taskList;

	// Constructor
	public TaskList() {
		taskList = new ArrayList<Task>();
		this.setID(java.util.UUID.randomUUID().toString());
	}

	// Getter & Setter
	public String getID() {
		return id;
	}

	private void setID(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Timestamp getUpdate() {
		return java.sql.Timestamp.valueOf(this.update);
	}

	public void setUpdate(Timestamp update) {
		this.update = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(update);
	}

	public boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	// Other Methodes
	public void addTaskToList(Task task) {
		taskList.add(task);
	}

	public boolean deleteTaskOfList(Task task) {
		return taskList.remove(task);
	}

	public boolean containsTaskInList(Task task) {
		return taskList.contains(task);
	}

	public List<Task> getTaskList() {
		return this.taskList;
	}
}
