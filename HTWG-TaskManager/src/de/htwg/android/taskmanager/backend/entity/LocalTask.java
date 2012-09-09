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
		this(java.util.UUID.randomUUID().toString());
		this.status = EStatus.NEEDS_ACTION;
	}

	public LocalTask(String internalId) {
		this.internalId = internalId;
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
	
	public void modifyCompleted(long completed) {
		lastModification = new Date().getTime();
		this.completed = completed;
	}
	
	public void modifyDue(long due) {
		lastModification = new Date().getTime();
		this.due = due;
	}
	
	public void modifyNotes(String notes) {
		lastModification = new Date().getTime();
		this.notes = notes;
	}
	
	public void modifyStatus(EStatus status) {
		lastModification = new Date().getTime();
		this.status = status;
	}
	
	public void modifyTitle(String title) {
		lastModification = new Date().getTime();
		this.title = title;
	}
	
	public void setCompleted(long completed) {
		this.completed = completed;
	}

	public void setDue(long due) {
		this.due = due;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}

	public void setLastModification(long lastModification) {
		this.lastModification = lastModification;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setStatus(EStatus status) {
		this.status = status;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
