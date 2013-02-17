package de.htwg.android.taskmanager.backend.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class represents a LocalTaskList DTO object.
 * 
 * @author Filippelli, Gerhart, Gillet
 * 
 */
public class LocalTaskList {

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
	 * The list of tasks for this task list.
	 */
	private List<LocalTask> taskList;

	/**
	 * Creates a new task list with a random UUID.
	 */
	public LocalTaskList() {
		this(java.util.UUID.randomUUID().toString());
	}

	/**
	 * Creates a new task list with an given id
	 * 
	 * @param internalId
	 *            the id.
	 */
	public LocalTaskList(String internalId) {
		taskList = new ArrayList<LocalTask>();
		this.internalId = internalId;
	}

	/**
	 * Adds a new task to the list of tasks for this list.
	 * 
	 * @param task
	 *            the task to add.
	 */
	public void addTaskToList(LocalTask task) {
		taskList.add(task);
	}

	/**
	 * Checks if a task is in the list of tasks for this task list.
	 * 
	 * @param task
	 *            the task to check
	 * @return true if it is in the task list, false otherwise.
	 */
	public boolean containsTaskInList(LocalTask task) {
		return taskList.contains(task);
	}

	/**
	 * Deletes a task added before in the list of tasks.
	 * 
	 * @param task
	 *            the task to delete
	 * @return true if it is been removed, false otherwise.
	 */
	public boolean deleteTaskOfList(LocalTask task) {
		return taskList.remove(task);
	}

	/**
	 * Equals Method, to check equality of task lists (checked by internalId).
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
		LocalTaskList other = (LocalTaskList) obj;
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
	 * Gets the title attribute.
	 * 
	 * @return the title attribute.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Gets the list of tasks
	 * 
	 * @return the list of tasks.
	 */
	public List<LocalTask> getTaskList() {
		return taskList;
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
	 * Sets the title for this task.
	 * 
	 * @param notes
	 *            the title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

}
