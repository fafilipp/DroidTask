package de.htwg.android.taskmanager.backend.entity;

import java.util.Date;

import org.simpleframework.xml.Element;

import de.htwg.android.taskmanager.backend.util.EStatus;

public class LocalTask {

	@Element
	private String internalId;
	@Element(required = false)
	private String googleId;
	@Element
	private long lastModification;
	@Element
	private String title;
	@Element(required = false)
	private String notes;
	@Element
	private EStatus status;
	@Element(required = false)
	private long due;
	@Element(required = false)
	private long completed;

	public LocalTask() {
		this.internalId = java.util.UUID.randomUUID().toString();
		this.status = EStatus.NEEDS_ACTION;
	}

	public long getCompleted() {
		return completed;
	}

	public long getDue() {
		return due;
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

	public String getNotes() {
		return notes;
	}

	public EStatus getStatus() {
		return status;
	}

	public String getTitle() {
		return title;
	}

	public void setCompleted(long completed) {
		lastModification = new Date().getTime();
		this.completed = completed;
	}

	public void setDue(long due) {
		lastModification = new Date().getTime();
		this.due = due;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}

	public void setNotes(String notes) {
		lastModification = new Date().getTime();
		this.notes = notes;
	}

	public void setStatus(EStatus status) {
		lastModification = new Date().getTime();
		this.status = status;
	}

	public void setTitle(String title) {
		lastModification = new Date().getTime();
		this.title = title;
	}

}
