package de.htwg.android.taskmanager.backend.entity;

import java.util.Date;

import de.htwg.android.taskmanager.backend.util.EStatus;

/**
 * This class represents a LocalTask DTO object.
 * 
 * @author Filippelli, Gerhart, Gillet
 * 
 */
public class LocalTask {

	/**
	 * The application internal id.
	 */
	private String internalId;

	/**
	 * The Google Tasks Api generated id. This id exists only, if the task is
	 * been synced with Google.
	 */
	private String googleId;

	/**
	 * The last modification date (necessary for synchronization). Will be
	 * changed while calling the modifyXXX methods.
	 */
	private long lastModification;

	/**
	 * The title of the task.
	 */
	private String title;

	/**
	 * The notes for this task.
	 */
	private String notes;

	/**
	 * The status of this task (COMPLETED, NEEDS_ACTION).
	 */
	private EStatus status;

	/**
	 * The due date for this task in millis.
	 */
	private long due;

	/**
	 * The completion date in millis.
	 */
	private long completed;

	/**
	 * Creates a new task with a random UUID.
	 */
	public LocalTask() {
		this(java.util.UUID.randomUUID().toString());
		this.status = EStatus.NEEDS_ACTION;
	}

	/**
	 * Creates a new task with an given id
	 * 
	 * @param internalId
	 *            the id.
	 */
	public LocalTask(String internalId) {
		this.internalId = internalId;
	}

	/**
	 * Equals Method, to check equality of tasks (checked by internalId).
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		LocalTask other = (LocalTask) obj;
		if (internalId == null) {
			if (other.internalId != null) {
				return false;
			}
		} else if (!internalId.equals(other.internalId)) {
			return false;
		}
		return true;
	}

	/**
	 * Gets the completed attribute.
	 * 
	 * @return the completed attribute.
	 */
	public long getCompleted() {
		return completed;
	}

	/**
	 * Gets the due attribute.
	 * 
	 * @return the due attribute.
	 */
	public long getDue() {
		return due;
	}

	/**
	 * Gets the google id attribute.
	 * 
	 * @return the google id attribute.
	 */
	public String getGoogleId() {
		return googleId;
	}

	/**
	 * Gets the internal id attribute.
	 * 
	 * @return the internal id attribute.
	 */
	public String getInternalId() {
		return internalId;
	}

	/**
	 * Gets the last modification attribute.
	 * 
	 * @return the last modification attribute.
	 */
	public long getLastModification() {
		return lastModification;
	}

	/**
	 * Gets the notes attribute.
	 * 
	 * @return the notes attribute.
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * Gets the status attribute.
	 * 
	 * @return the status attribute.
	 */
	public EStatus getStatus() {
		return status;
	}

	/**
	 * Gets the title attribute.
	 * 
	 * @return the title attribute.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * The hashcode method for this task (by internalId).
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((internalId == null) ? 0 : internalId.hashCode());
		return result;
	}

	/**
	 * Modifies the completion date for this task. While calling the
	 * modify-Method the last modification date will be updated to 'now'.
	 * 
	 * @param completed
	 *            the completion value to set.
	 */
	public void modifyCompleted(long completed) {
		lastModification = new Date().getTime();
		setCompleted(completed);
	}

	/**
	 * Modifies the due date for this task. While calling the modify-Method the
	 * last modification date will be updated to 'now'.
	 * 
	 * @param due
	 *            the due date to set.
	 */
	public void modifyDue(long due) {
		lastModification = new Date().getTime();
		setDue(due);
	}

	/**
	 * Modifies the notes for this task. While calling the modify-Method the
	 * last modification date will be updated to 'now'.
	 * 
	 * @param notes
	 *            the notes to set.
	 */
	public void modifyNotes(String notes) {
		lastModification = new Date().getTime();
		setNotes(notes);
	}

	/**
	 * Modifies the status for this task. While calling the modify-Method the
	 * last modification date will be updated to 'now'.
	 * 
	 * @param status
	 *            the status value to set.
	 */
	public void modifyStatus(EStatus status) {
		lastModification = new Date().getTime();
		setStatus(status);
	}

	/**
	 * Modifies the title for this task. While calling the modify-Method the
	 * last modification date will be updated to 'now'.
	 * 
	 * @param title
	 *            the title to set.
	 */
	public void modifyTitle(String title) {
		lastModification = new Date().getTime();
		setTitle(title);
	}

	/**
	 * Sets the completion date for this task.
	 * 
	 * @param completed
	 *            the completion date to set.
	 */
	public void setCompleted(long completed) {
		this.completed = completed;
	}

	/**
	 * Sets the due date for this task.
	 * 
	 * @param due
	 *            the due date to set.
	 */
	public void setDue(long due) {
		this.due = due;
	}

	/**
	 * Sets the google id for this task.
	 * 
	 * @param googleId
	 *            the google id to set.
	 */
	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}

	/**
	 * Sets the last modification date for this task.
	 * 
	 * @param lastModification
	 *            the last modification date to set.
	 */
	public void setLastModification(long lastModification) {
		this.lastModification = lastModification;
	}

	/**
	 * Sets the notes for this task.
	 * 
	 * @param notes
	 *            the notes to set.
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * Sets the status for this task.
	 * 
	 * @param notes
	 *            the status to set.
	 */
	public void setStatus(EStatus status) {
		this.status = status;
	}

	/**
	 * Sets the title for this task.
	 * 
	 * @param notes
	 *            the title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

}
