package de.htwg.android.taskmanager.google.task.api;

import java.util.List;

import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;

import de.htwg.android.taskmanager.google.task.api.util.GoogleSyncException;

/**
 * This interface defines the methods which can be called to an external Api.
 * 
 * @author Filippelli, Gerhart, Gillet
 * 
 */
public interface ITaskApiManager {

	/**
	 * Deletes a remote task with given ids.
	 * 
	 * @param taskListId
	 *            the task list remote id.
	 * @param taskId
	 *            the task remote id.
	 * @throws GoogleSyncException
	 *             will be thrown if something goes wrong while executing.
	 */
	public void deleteTask(String taskListId, String taskId) throws GoogleSyncException;

	/**
	 * Deletes a remote task list with a given id.
	 * 
	 * @param taskListId
	 *            the task list remote id.
	 * @throws GoogleSyncException
	 *             will be thrown if something goes wrong while executing.
	 */
	public void deleteTaskList(String taskListId) throws GoogleSyncException;

	/**
	 * Gets all remote task lists.
	 * 
	 * @return the remote task lists (in the remote representation).
	 * @throws GoogleSyncException
	 *             will be thrown if something goes wrong while executing.
	 */
	public List<TaskList> getTaskLists() throws GoogleSyncException;

	/**
	 * Gets all remote tasks for a given task list id.
	 * 
	 * @param taskListId
	 *            the task list id.
	 * @return a list of tasks (in the remote representation).
	 * @throws GoogleSyncException
	 *             will be thrown if something goes wrong while executing.
	 */
	public List<Task> getTasks(String taskListId) throws GoogleSyncException;

	/**
	 * Inserts a remote task in a task list.
	 * 
	 * @param taskListId
	 *            the remote task list id
	 * @param task
	 *            the task to be add in the remote representation.
	 * @return the Task after insertion with remote id.
	 * @throws GoogleSyncException
	 *             will be thrown if something goes wrong while executing.
	 */
	public Task insertTask(String taskListId, Task task) throws GoogleSyncException;

	/**
	 * Inserts a remote task list with a given title.
	 * 
	 * @param title
	 *            the title for this task list.
	 * @return the TaskList insertion with remote id.
	 * @throws GoogleSyncException
	 *             will be thrown if something goes wrong while executing.
	 */
	public TaskList insertTaskList(String title) throws GoogleSyncException;

	/**
	 * Updates a remote task.
	 * 
	 * @param taskListId
	 *            the task list id where the task is into.
	 * @param taskId
	 *            the task id of the task which should be updated.
	 * @param task
	 *            the updated task object
	 * @return the updated Task object.
	 * @throws GoogleSyncException
	 *             will be thrown if something goes wrong while executing.
	 */
	public Task updateTask(String taskListId, String taskId, Task task) throws GoogleSyncException;

	/**
	 * Updates a remote task list.
	 * 
	 * @param taskListId
	 *            the task list id which should be updated.
	 * @param taskList
	 *            the updated task list object
	 * @return the updated TaskList object.
	 * @throws GoogleSyncException
	 *             will be thrown if something goes wrong while executing.
	 */
	public TaskList updateTaskList(String taskListId, TaskList taskList) throws GoogleSyncException;
}
