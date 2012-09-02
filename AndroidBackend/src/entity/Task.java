package entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.simpleframework.xml.Element;

public class Task {

	// Unique ID of a Task
	@Element
	private String id;
	// Timestamp of last Synchronisation with Google
	@Element(required = false)
	private String lastSynchOnline;
	// Timestamp of last Modification offline
	@Element
	private String lastModification;
	// Title of the Task
	@Element
	private String title;
	// The parent Task if there is some
	@Element(required = false)
	private String parentTask;
	// The position of the Task, if it is a subtask
	@Element
	private String position;
	// Describing the Task
	@Element(required = false)
	private String notes;
	// Status of the Task see Enum
	@Element
	private EStatus status;
	// Due Date of the Task
	@Element(required = false)
	private String due;
	// Completed Date
	@Element(required = false)
	private String completed;
	// Task deleted
	@Element
	private boolean deleted;
	// Task finished
	@Element
	private boolean hidden;

	// Constructor
	public Task() {
		this.setId(java.util.UUID.randomUUID().toString());
	}

	// Getter & Setter
	public String getId() {
		return id;
	}

	private void setId(String id) {
		this.id = id;
	}

	public Timestamp getLastSynchOnline() {
		return java.sql.Timestamp.valueOf(this.lastSynchOnline);
	}

	public void setLastSynchOnline(Timestamp lastSynchOnline) {
		// Parse Timestamp to Google Format
		this.lastSynchOnline = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(lastSynchOnline);
	}

	public Timestamp getLastModification() {
		return java.sql.Timestamp.valueOf(this.lastModification);
	}

	public void setLastModification(Timestamp lastModification) {
		this.lastModification = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(lastModification);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getParentTask() {
		return parentTask;
	}

	public void setParentTask(String parentTask) {
		this.parentTask = parentTask;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public EStatus getStatus() {
		return status;
	}

	public void setStatus(EStatus status) {
		this.status = status;
	}

	public Timestamp getDue() {
		if (this.due == null) {
			return null;
		}
		return java.sql.Timestamp.valueOf(this.due);
	}

	public void setDue(Timestamp due) {
		if (due != null) {
			this.due = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(due);
		} else {
			this.due = null;
		}
	}

	public Timestamp getCompleted() {
		if (this.completed == null) {
			return null;
		}
		return java.sql.Timestamp.valueOf(this.completed);
	}

	public void setCompleted(Timestamp completed) {
		if (completed != null) {
			this.completed = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(completed);
		} else {
			this.completed = null;
		}
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

}
